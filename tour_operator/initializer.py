import json
import os
import time

from functions import generate_trips
from flights.GetFlightsEvent import GetFlightsEvent
from hotels.GetHotelsEvent import GetHotelsEvent


class Initializer:
    """
    Initializer is used to load scrapped flights and hotels into queues
    with names specified in Dockerfile as environment variables.
    It also creates possible tour combinations.
    """
    def __init__(self, to, wait_time=5):
        self.to = to
        time.sleep(wait_time)
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
                self.to.channel.basic_publish(exchange='', routing_key=self.to.add_hotel_queue, body=message)
                self.hotel_number += 1

        with open(os.path.join(os.path.dirname(__file__), 'init_data/flights.json'), 'r') as file:
            self.flights_number = 0
            for line in file:
                message = json.dumps(json.loads(line))
                self.to.channel.basic_publish(exchange='', routing_key=self.to.add_flight_queue, body=message)
                self.flights_number += 1

    def generate_trips(self, wait_time=5):
        """
        based on existing hotels and flights in databases generates trips
        :return:
        """
        time.sleep(wait_time)
        flights, hotels = [], []
        while len(flights) < self.flights_number:
            flights = GetFlightsEvent(self.to.channel, self.to.callback_queue).flights
            time.sleep(2)
        while len(hotels) < self.hotel_number:
            hotels = GetHotelsEvent(self.to.channel, self.to.callback_queue).hotels
            time.sleep(2)

        if flights is [] or flights is None or hotels is [] or hotels is None:
            print("No combinations for initial trips!!!")
            return

        generate_trips(self.to.channel, self.to.add_trip_queue, hotels, flights)
        print("Finished generating trips")
