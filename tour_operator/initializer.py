import json
import os
from functions import send_message_and_get_response


class Initializer:
    """
    Initializer is used to load scrapped flights and hotels into queues
    with names specified in Dockerfile as environment variables.
    It also creates possible tour combinations.
    """
    def __init__(self, to):
        self.to = to
        print("Start initialization")
        self.load_data()
        self.generate_trips()

    def load_data(self):
        with open(os.path.join(os.path.dirname(__file__), 'init_data/hotels.json'), 'r') as file:
            for line in file:
                message = json.dumps(json.loads(line))
                self.to.channel.basic_publish(exchange='', routing_key=self.to.add_hotel_queue, body=message)

        with open(os.path.join(os.path.dirname(__file__), 'init_data/flights.json'), 'r') as file:
            for line in file:
                message = json.dumps(json.loads(line))
                self.to.channel.basic_publish(exchange='', routing_key=self.to.add_flight_queue, body=message)

    def generate_trips(self):
        flights = send_message_and_get_response(self.to.channel, self.to.get_flights_queue, self.to.callback_queue)
        hotels = send_message_and_get_response(self.to.channel, self.to.get_hotels_queue, self.to.callback_queue)
        if flights is [] or flights is None or hotels is [] or hotels is None:
            print("No combinations for initial trips!!!")
            return

        print("Init trips generation")
