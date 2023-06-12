import json
import os
from functions import send_message_and_get_response
from trips.Trip import Trip


class GetTripsEvent:
    def __init__(self, channel, callback_queue):
        self.get_trips_queue = os.environ.get('GET_TRIPS_QUEUE', 'GetTripsQueue')
        trips = json.loads(send_message_and_get_response(channel, self.get_trips_queue, callback_queue))
        if trips is not None and trips is not []:
            self.trips = [Trip(trip) for trip in trips["trips"]]
