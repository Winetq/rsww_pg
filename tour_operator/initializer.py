import json
import os
import time
import pika

from flights.Flight import Flight
from functions import generate_trips, send_message_and_get_response
from hotels.Hotel import Hotel


class Initializer:
    """
    Initializer is used to load scrapped flights and hotels into queues
    with names specified in Dockerfile as environment variables.
    It also creates possible tour combinations.
    """
    def __init__(self):
        self.add_hotel_queue = "AddHotelQueue"
        self.add_flight_queue = "AddFlightQueue"
        self.add_trip_queue = "AddTripQueue"
        self.get_hotels_queue = 'GetHotelsQueue'
        self.get_flights_queue = 'GetFlightsQueue'

        connection = pika.BlockingConnection(pika.ConnectionParameters('localhost'))
        self.channel = connection.channel()
        result = self.channel.queue_declare(queue='', exclusive=True)
        self.callback_queue = result.method.queue

        print("Start initialization")
        self.load_data()
        self.generate_trips(wait_time=5)

    def load_data(self):
        """
        loads scrapped init data from files
        :return:
        """
        with open(os.path.join(os.path.dirname(__file__), 'init_data/hotels.json'), 'r') as file:
            self.hotel_number = 0
            for line in file:
                message = json.dumps(json.loads(line))
                self.channel.basic_publish(exchange='', routing_key=self.add_hotel_queue, body=message)
                self.hotel_number += 1

        with open(os.path.join(os.path.dirname(__file__), 'init_data/flights.json'), 'r') as file:
            self.flights_number = 0
            for line in file:
                message = json.dumps(json.loads(line))
                self.channel.basic_publish(exchange='', routing_key=self.add_flight_queue, body=message)
                self.flights_number += 1

    def get_hotels(self):
        hotels = json.loads(send_message_and_get_response(self.channel, self.get_hotels_queue, self.callback_queue))
        if hotels is not None and hotels is not []:
            hotels = [Hotel(json.dumps(hotel)) for hotel in hotels["hotels"]]
        return hotels

    def get_flights(self):
        flights = json.loads(send_message_and_get_response(self.channel, self.get_flights_queue, self.callback_queue))
        if flights is not [] and flights is not None:
            flights = [Flight(flight) for flight in flights]
        return flights

    def generate_trips(self, wait_time=5):
        """
        based on existing hotels and flights in databases generates trips
        :return:
        """
        time.sleep(wait_time)
        flights, hotels = [], []
        while len(flights) < self.flights_number:
            flights = self.get_flights()
            time.sleep(2)
        while len(hotels) < self.hotel_number:
            hotels = self.get_hotels()
            time.sleep(2)

        if flights is [] or flights is None or hotels is [] or hotels is None:
            print("No combinations for initial trips!!!")
            return

        generate_trips(self.channel, self.add_trip_queue, hotels, flights)
        print("Finished generating trips")


if __name__ == "__main__":
    Initializer()
