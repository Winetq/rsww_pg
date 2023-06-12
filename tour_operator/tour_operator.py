import os
import json
import time
import pika
import random
from initializer import Initializer


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
        self.get_hotels_queue = os.environ.get('GET_HOTELS_QUEUE', 'GetHotelsQueue')
        self.get_flights_queue = os.environ.get('GET_FLIGHTS_QUEUE', 'GetFlightsQueue')

        self.min_wait_time = os.environ.get('MIN_WAIT_TIME', 3)
        self.max_wait_time = os.environ.get('MAX_WAIT_TIME', 8)

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

    def add_hotel(self, hotel_id):
        file = open('to_data/hotels.json', 'r')
        lines = file.readlines()
        if len(lines) > hotel_id:
            message = json.loads(json.dumps(lines[hotel_id]))
            self.channel.basic_publish(exchange='', routing_key=self.add_hotel_queue, body=message)
            #TODO: generate trips with hotel
            return True
        return False

    def add_flight(self, flight_id):
        file = open('to_data/flights.json', 'r')
        lines = file.readlines()
        if len(lines) > flight_id:
            message = json.loads(json.dumps(lines[flight_id]))
            self.channel.basic_publish(exchange='', routing_key=self.add_flight_queue, body=message)
            #TODO: generate trips with flight
            return True
        return False

    def delete_trip(self):
        #TODO
        pass

    def change_room_price(self):
        #TODO
        pass

    def change_flight_price(self):
        #TODO
        pass

    def generate(self):
        hotel_file_id = 0
        flight_file_id = 0
        while True:
            time.sleep(random.randint(self.min_wait_time, self.max_wait_time))
            probability_value = random.randint(0, 5)
            if probability_value == 0:
                if self.add_hotel(hotel_file_id):
                    print("TO added hotel")
                    hotel_file_id += 1
            elif probability_value == 1:
                if self.add_flight(flight_file_id):
                    print("TO added flight")
                    flight_file_id += 1
            elif probability_value == 2:
                # print("TO deleted trip")
                self.delete_trip()
            elif probability_value == 3:
                # print("TO changed price of hotel room")
                self.change_room_price()
            else:
                # print("TO changed price of flight")
                self.change_flight_price()

    def close_connection(self):
        self.channel.close()
        self.connection.close()


if __name__ == "__main__":
    tour_operator = TourOperator()
    time.sleep(2)
    Initializer(tour_operator)
    print("Start of generating changes by TO")
    # tour_operator.generate()
    tour_operator.close_connection()
