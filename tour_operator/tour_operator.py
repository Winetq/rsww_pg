import os
import json
import time
import pika
import random

from flights.GetFlightsEvent import GetFlightsEvent
from flights.UpdateFlightPriceEvent import UpdateFlightPriceEvent
from hotels.GetHotelDetailsEvent import GetHotelDetailsEvent
from hotels.GetHotelsEvent import GetHotelsEvent
from hotels.Hotel import Hotel
from hotels.HotelDetails import HotelDetails
from hotels.UpdateRoomPriceEvent import UpdateRoomPriceEvent
from initializer import Initializer
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

    def add_hotel(self, hotel):
        message = json.loads(json.dumps(hotel))
        self.channel.basic_publish(exchange='', routing_key=self.add_hotel_queue, body=message)

        hotel = HotelDetails(hotel)
        hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
        hotel = [h for h in hotels if h.compare(hotel)]
        if len(hotel) > 0:
            hotel = hotel[0]
            # add trip combinations
            flights = GetFlightsEvent(self.channel, self.callback_queue).flights
            generate_trips_for_hotel(self.channel, self.add_trip_queue, hotel, flights)
            return True
        return False

    def add_flight(self, flight):
        message = json.loads(json.dumps(flight))
        self.channel.basic_publish(exchange='', routing_key=self.add_flight_queue, body=message)

        flights = GetFlightsEvent(self.channel, self.callback_queue).flights
        flight = [f for f in flights if f.compare(flight)]
        if len(flight) > 0:
            flight = flight[0]
            # add trip combinations
            hotels = GetHotelsEvent(self.channel, self.callback_queue).hotels
            generate_trips_for_flight(self.channel, self.add_trip_queue, flight, hotels, flights)
            return True
        return False

    def delete_trip(self):
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
        UpdateFlightPriceEvent(self.channel, flight_to_update, new_price)

    def generate(self):
        hotel_file = open('to_data/hotels.json', 'r')
        hotels = hotel_file.readlines()
        hotel_file_id = 0

        flight_file = open('to_data/flights.json', 'r')
        flights = flight_file.readlines()
        flight_file_id = 0

        while True:
            time.sleep(random.randrange(self.min_wait_time, self.max_wait_time))
            probability_value = random.randint(0, 5)
            if probability_value == 0:
                if len(hotels) > hotel_file_id:
                    if self.add_hotel(hotels[hotel_file_id]):
                        print("TO added hotel")
                        hotel_file_id += 1
                    else:
                        print("TO tried to add hotel")
            elif probability_value == 1:
                if len(flights) > flight_file_id:
                    if self.add_flight(flights[flight_file_id]):
                        print("TO added flight")
                        flight_file_id += 1
                    else:
                        print("TO tried to add flight")
            elif probability_value == 2:
                if self.delete_trip():
                    print("TO deleted trip")
                else:
                    print("TO tried to delete trip")
            # elif probability_value == 3:
            #     print("TO changed price of hotel room")
            #     # self.change_room_price()
            # else:
            #     self.change_flight_price()
            #     print("TO changed price of flight")

    def close_connection(self):
        self.channel.close()
        self.connection.close()


if __name__ == "__main__":
    tour_operator = TourOperator()
    Initializer(tour_operator, 5)
    # print("Start of generating changes by TO")
    # tour_operator.generate()
    # tour_operator.close_connection()
