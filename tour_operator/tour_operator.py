import os
import json
import time
import pika
import random

from flights.GetFlightsEvent import GetFlightsEvent
from flights.UpdateFlightPriceEvent import UpdateFlightPriceEvent
from hotels.GetHotelDetailsEvent import GetHotelDetailsEvent
from hotels.GetHotelsEvent import GetHotelsEvent
from hotels.HotelDetails import HotelDetails
from hotels.UpdateRoomPriceEvent import UpdateRoomPriceEvent
from functions import generate_trips_for_hotel, generate_trips_for_flight
from to.GetTOLatestUpdatesEvent import GetTOLatestUpdatesEvent
from to.TOLatestUpdates import TOLatestUpdates
from trips.DeleteTripEvent import DeleteTripEvent
from trips.GetTripsEvent import GetTripsEvent


class TourOperator:
    """
    TourOperator is used to load initial data using Initializer
    and to generate random changes every now and then:
    - adding scrapped hotel and combinations of trips with new hotel
    - adding scrapped flight and combinations of trips with new flight
    - deleting random trip
    - changing prices of random room
    - changing prices of random flight
    """
    def __init__(self):
        self.address = os.environ.get('AMQP_URL', '127.0.0.1')
        self.add_hotel_queue = os.environ.get('ADD_HOTEL_QUEUE', 'AddHotelQueue')
        self.add_flight_queue = os.environ.get('ADD_FLIGHT_QUEUE', 'AddFlightQueue')
        self.add_trip_queue = os.environ.get('ADD_TRIP_QUEUE', 'AddTripQueue')
        self.get_TO_latest_updates_queue = os.environ.get('GET_TO_LATEST_UPDATES_QUEUE', 'GetTOLatestUpdatesQueue')

        self.min_wait_time = int(os.environ.get('MIN_WAIT_TIME', 3))
        self.max_wait_time = int(os.environ.get('MAX_WAIT_TIME', 8))
        self.sleep_time = float(os.environ.get('SLEEP_TIME', 0.5))
        self.TO_latest_updates = TOLatestUpdates()

        self.reconnection_tries = int(os.environ.get('AMQP_RECONNECTION_TRIES', 10))
        self.reconnection_delay = int(os.environ.get('AMQP_RECONNECTION_DELAY', 5))
        self.connection_tries = 0
        self.connection = None
        self.channel = None
        self.callback_queue = None
        self.make_connection()

    def make_connection(self):
        try:
            self.connection_tries += 1
            params = pika.ConnectionParameters(self.address)
            self.connection = pika.BlockingConnection(params)
            self.channel = self.connection.channel()
            self.channel.queue_declare(queue=self.get_TO_latest_updates_queue)
            result = self.channel.queue_declare(queue='', exclusive=True)
            self.callback_queue = result.method.queue
        except pika.exceptions.AMQPConnectionError:
            if self.connection_tries < self.reconnection_tries:
                print(f'Could not connect to AMQP server. Trying again after {self.reconnection_delay} seconds')
                time.sleep(self.reconnection_delay)
                self.make_connection()
            else:
                raise ConnectionError('AMQP server is unreachable. Max reconnection tries have been reached')

    def wait_until_hotel_added(self, hotel):
        hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
        result = [h for h in hotels if h.compare(hotel)]
        while len(result) <= 0:
            hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
            result = [h for h in hotels if h.compare(hotel)]
            time.sleep(self.sleep_time)
        return result[0]

    def check_if_hotel_exist(self, hotels_file, hotel_file_id):
        hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
        if len(hotels) < 0:
            return False, hotel_file_id
        # check if hotel already has been added
        hotel_details = HotelDetails(hotels_file[hotel_file_id])
        hotel = [h for h in hotels if h.compare(hotel_details)]
        if len(hotel) <= 0:
            return False, hotel_file_id

        # look for another hotel that has not been added yet
        while len(hotel) > 0 and len(hotels_file) > hotel_file_id:
            hotel_file_id += 1
            hotel_details = HotelDetails(hotels_file[hotel_file_id])
            hotel = [h for h in hotels if h.compare(hotel_details)]
        if len(hotel) <= 0:
            return False, hotel_file_id
        return True, hotel_file_id

    def add_hotel(self, hotels_file, hotel_file_id):
        hotel_exist, hotel_file_id = self.check_if_hotel_exist(hotels_file, hotel_file_id)
        if hotel_exist or len(hotels_file) <= hotel_file_id:
            return hotel_file_id

        # add new hotel
        message = json.loads(json.dumps(hotels_file[hotel_file_id]))
        self.channel.basic_publish(exchange='', routing_key=self.add_hotel_queue, body=message)
        hotel = self.wait_until_hotel_added(HotelDetails(hotels_file[hotel_file_id]))

        # add trip combinations
        flights = GetFlightsEvent(self.channel, self.callback_queue).flights
        generate_trips_for_hotel(self.channel, self.add_trip_queue, hotel, flights)
        self.TO_latest_updates.add_update("Dodano hotel " + str(hotel.name))
        return hotel_file_id

    def wait_until_flight_added(self, flight):
        flights = GetFlightsEvent(self.channel, self.callback_queue).flights
        result = [f for f in flights if f.compare(flight)]
        while len(result) <= 0:
            flights = GetFlightsEvent(self.channel, self.callback_queue).flights
            result = [f for f in flights if f.compare(flight)]
            time.sleep(self.sleep_time)
        return result[0], flights

    def check_if_flight_exist(self, flights_file, flight_file_id):
        flights = GetFlightsEvent(self.channel, self.callback_queue).flights
        if len(flights) <= 0:
            return False, flight_file_id
        # check if flight already has been added
        flight = [f for f in flights if f.compare(flights_file[flight_file_id])]
        if len(flight) <= 0:
            return False, flight_file_id

        # look for another flight that has not been added yet
        while len(flight) > 0 and len(flights_file) > flight_file_id:
            flight_file_id += 1
            flight = [f for f in flights if f.compare(flights_file[flight_file_id])]
        if len(flight) <= 0:
            return False, flight_file_id
        return True, flight_file_id

    def add_flight(self, flights_file, flight_file_id):
        flight_exist, flight_file_id = self.check_if_flight_exist(flights_file, flight_file_id)
        if flight_exist or len(flights_file) <= flight_file_id:
            return flight_file_id

        # add new flight
        message = json.loads(json.dumps(flights_file[flight_file_id]))
        self.channel.basic_publish(exchange='', routing_key=self.add_flight_queue, body=message)
        flight, flights = self.wait_until_flight_added(flights_file[flight_file_id])

        # add trip combinations
        hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
        generate_trips_for_flight(self.channel, self.add_trip_queue, flight, hotels, flights)
        self.TO_latest_updates.add_update(
            "Dodano lot z " + str(flight.departureAirport) + " do " + str(flight.arrivalAirport))
        return flight_file_id

    def remove_trip(self):
        trips = GetTripsEvent(self.channel, self.callback_queue).trips
        if len(trips) > 0:
            trip_to_delete = random.choice(trips)
            DeleteTripEvent(self.channel, trip_to_delete)
            self.TO_latest_updates.add_update("Usunieto wycieczke o id " + str(trip_to_delete.id))
            return True
        return False

    def change_room_price(self):
        hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
        hotel_to_update = random.choice(hotels)

        hotel_details = GetHotelDetailsEvent(self.channel, self.callback_queue, hotel_to_update).hotel_details
        rooms = hotel_details.rooms
        room_to_update = random.choice(rooms)

        new_price = room_to_update.price * random.randrange(80, 120) // 100
        if new_price == room_to_update.price:
            new_price = room_to_update.price * 90 // 100

        self.TO_latest_updates.add_update(
            "Zmieniono cene pokoju " + str(room_to_update.name) + " w hotelu " + str(hotel_details.name) +
            " z " + str(room_to_update.price) + " na " + str(new_price))
        UpdateRoomPriceEvent(self.channel, hotel_to_update.id, room_to_update, new_price)

    def change_flight_price(self):
        flights = GetFlightsEvent(self.channel, self.callback_queue).flights
        if len(flights) <= 0:
            return False
        flight_to_update = random.choice(flights)
        new_price = flight_to_update.price * random.randrange(80, 120) // 100
        if new_price == flight_to_update.price:
            new_price = flight_to_update.price * 90 // 100
        UpdateFlightPriceEvent(self.channel, flight_to_update, new_price)
        self.TO_latest_updates.add_update(
            "Zmieniono cene lotu " + str(flight_to_update.departureAirport) + " - " +
            str(flight_to_update.arrivalAirport) + " z " + str(flight_to_update.price) + " na " + str(new_price))
        return True

    def read_data_generation(self):
        hotel_file = open('to_data/hotels.json', 'r')
        hotels = hotel_file.readlines()
        flight_file = open('to_data/flights.json', 'r')
        flights = flight_file.readlines()
        return hotels, flights

    def wait_for_trips(self):
        trips = []
        while trips is None or len(trips) <= 0:
            trips = GetTripsEvent(self.channel, self.callback_queue).trips
            self.listen()
            time.sleep(self.sleep_time)

    def listen(self):
        method_frame, header_frame, body = self.channel.basic_get(queue=self.get_TO_latest_updates_queue, auto_ack=True)
        if method_frame is not None and header_frame is not None:
            reply_to_queue = header_frame.reply_to
            print("TO sended latest updates to " + str(reply_to_queue))
            GetTOLatestUpdatesEvent(self.channel, reply_to_queue, header_frame, self.TO_latest_updates.latest_updates)

    def generate(self):
        hotel_file_id, flight_file_id = 0, 0
        hotels, flights = self.read_data_generation()
        self.wait_for_trips()
        print("Start of generating changes by TO")

        while True:
            event_time = random.randrange(self.min_wait_time, self.max_wait_time)
            check_time = 0
            while check_time < event_time:
                self.listen()
                time.sleep(self.sleep_time)
                check_time += self.sleep_time

            probability_value = random.randint(0, 4)
            print("Random: " + str(probability_value))
            if probability_value == 0:
                if len(hotels) > hotel_file_id:
                    hotel_file_id = self.add_hotel(hotels, hotel_file_id)
                    if len(hotels) > hotel_file_id:
                        print("TO added hotel")
                        hotel_file_id += 1
            elif probability_value == 1:
                if len(flights) > flight_file_id:
                    flight_file_id = self.add_flight(flights, flight_file_id)
                    if len(flights) > flight_file_id:
                        print("TO added flight")
                        flight_file_id += 1
            elif probability_value == 2:
                if self.change_flight_price():
                    print("TO changed price of flight")
            elif probability_value == 3:
                if self.remove_trip():
                    print("TO removed trip")
            # elif probability_value == 4:
            #     self.change_room_price()
            #     print("TO changed price of hotel room")

    def close_connection(self):
        self.channel.close()
        self.connection.close()


if __name__ == "__main__":
    tour_operator = TourOperator()
    tour_operator.generate()
    # tour_operator.close_connection()
