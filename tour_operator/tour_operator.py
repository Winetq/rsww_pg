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

        self.min_wait_time = int(os.environ.get('MIN_WAIT_TIME', 3))
        self.max_wait_time = int(os.environ.get('MAX_WAIT_TIME', 8))

        self.reconnection_tries = int(os.environ.get('AMQP_RECONNECTION_TRIES', 10))
        self.reconnection_delay = int(os.environ.get('AMQP_RECONNECTION_DELAY', 5))
        self.connection_tries = 0
        self.connection = None
        self.channel = None
        self.make_connection()

    def make_connection(self):
        try:
            self.connection_tries += 1
            params = pika.ConnectionParameters(self.address)
            self.connection = pika.BlockingConnection(params)
            self.channel = self.connection.channel()
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
            time.sleep(1)
        return result[0]

    def add_hotel(self, hotel):
        message = json.loads(json.dumps(hotel))
        self.channel.basic_publish(exchange='', routing_key=self.add_hotel_queue, body=message)

        hotel = HotelDetails(hotel)
        hotel = self.wait_until_hotel_added(hotel)

        # add trip combinations
        flights = GetFlightsEvent(self.channel, self.callback_queue).flights
        generate_trips_for_hotel(self.channel, self.add_trip_queue, hotel, flights)

    def wait_until_flight_added(self, flight):
        flights = GetFlightsEvent(self.channel, self.callback_queue).flights
        result = [f for f in flights if f.compare(flight)]
        while len(result) <= 0:
            flights = GetFlightsEvent(self.channel, self.callback_queue).flights
            result = [f for f in flights if f.compare(flight)]
            time.sleep(1)
        return result[0], flights

    def add_flight(self, flight):
        message = json.loads(json.dumps(flight))
        self.channel.basic_publish(exchange='', routing_key=self.add_flight_queue, body=message)

        flight, flights = self.wait_until_flight_added(flight)

        # add trip combinations
        hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
        generate_trips_for_flight(self.channel, self.add_trip_queue, flight, hotels, flights)

    def remove_trip(self):
        trips = GetTripsEvent(self.channel, self.callback_queue).trips
        if len(trips) > 0:
            trip_to_delete = random.choice(trips)
            DeleteTripEvent(self.channel, trip_to_delete)
            return True
        return False

    def change_room_price(self):
        hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
        hotel_to_update = random.choice(hotels)

        hotel_details = GetHotelDetailsEvent(self.channel, self.callback_queue, hotel_to_update).hotel_details
        rooms = hotel_details.getRooms()
        room_to_update = random.choice(rooms)

        new_price = room_to_update.basePrice * random.randrange(80, 120) // 100
        if new_price == room_to_update.basePrice:
            new_price = room_to_update.basePrice * 90 // 100

        UpdateRoomPriceEvent(self.channel, hotel_to_update.id, room_to_update, new_price)

    def change_flight_price(self):
        flights = GetFlightsEvent(self.channel, self.callback_queue).flights
        flight_to_update = random.choice(flights)
        new_price = flight_to_update.price * random.randrange(80, 120) // 100
        if new_price == flight_to_update.price:
            new_price = flight_to_update.price * 90 // 100
        print("Lot " + str(flight_to_update.id) + "   ---   " + str(flight_to_update.price) + " : " + str(new_price))
        UpdateFlightPriceEvent(self.channel, flight_to_update, new_price)

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
            time.sleep(0.5)

    def generate(self):
        hotel_file_id, flight_file_id = 0, 0
        hotels, flights = self.read_data_generation()
        self.wait_for_trips()
        print("Start of generating changes by TO")

        while True:
            event_time = random.randrange(self.min_wait_time, self.max_wait_time)
            time.sleep(event_time)
            # check_time = 0
            # while check_time < event_time:
            #     # sprawdz kolejke
            #     time.sleep(0.5)
            #     check_time += 0.5

            probability_value = random.randint(0, 2)
            print("Random: " + str(probability_value))
            if probability_value == 0:
                if len(hotels) > hotel_file_id:
                    self.add_hotel(hotels[hotel_file_id])
                    print("TO added hotel")
                    hotel_file_id += 1
            elif probability_value == 1:
                if len(flights) > flight_file_id:
                    self.add_flight(flights[flight_file_id])
                    print("TO added flight")
                    flight_file_id += 1
            elif probability_value == 2:
                self.change_flight_price()
                print("TO changed price of flight")
            # elif probability_value == 3:
            #     if self.remove_trip():
            #         print("TO removed trip")
            # elif probability_value == 4:
            #     print("TO changed price of hotel room")
            #     # self.change_room_price()

    def close_connection(self):
        self.channel.close()
        self.connection.close()


if __name__ == "__main__":
    tour_operator = TourOperator()
    # tour_operator.generate()
    # tour_operator.close_connection()
