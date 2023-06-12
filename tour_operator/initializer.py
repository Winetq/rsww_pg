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
    def __init__(self, to):
        self.to = to
        time.sleep(5)
        print("Start initialization")
        self.load_data()
        # self.generate_trips(wait_time=3)

    def load_data(self):
        """
        loads scrapped init data from files
        :return:
        """
        with open(os.path.join(os.path.dirname(__file__), 'init_data/hotels.json'), 'r') as file:
            for line in file:
                message = json.dumps(json.loads(line))
                self.to.channel.basic_publish(exchange='', routing_key=self.to.add_hotel_queue, body=message)

        with open(os.path.join(os.path.dirname(__file__), 'init_data/flights.json'), 'r') as file:
            for line in file:
                message = json.dumps(json.loads(line))
                self.to.channel.basic_publish(exchange='', routing_key=self.to.add_flight_queue, body=message)

    def generate_trips(self, wait_time=3):
        """
        based on existing hotels and flights in databases generates trips
        :return:
        """
        time.sleep(wait_time)
        flights = GetFlightsEvent(self.to.channel, self.to.callback_queue).flights
        hotels = GetHotelsEvent(self.to.channel, self.to.callback_queue).hotels
        if flights is [] or flights is None or hotels is [] or hotels is None:
            print("No combinations for initial trips!!!")
            return

        generate_trips(self.to.channel, self.to.add_trip_queue, hotels, flights)
