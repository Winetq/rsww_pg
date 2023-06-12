import json
import os
from flights.Flight import Flight
from functions import send_message_and_get_response


class GetFlightsEvent:
    def __init__(self, channel, callback_queue):
        self.get_flights_queue = os.environ.get('GET_FLIGHTS_QUEUE', 'GetFlightsQueue')
        flights = json.loads(send_message_and_get_response(channel, self.get_flights_queue, callback_queue))
        if flights is not [] and flights is not None:
            self.flights = [Flight(flight) for flight in flights]
