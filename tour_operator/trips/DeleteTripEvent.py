import json
import os


class DeleteTripEvent:
    def __init__(self, channel, trip):
        self.remove_trip_queue = os.environ.get('REMOVE_TRIP_QUEUE', 'RemoveTripQueue')
        trip_event = {
            "tripId": trip.id
        }
        message = json.dumps(trip_event)
        channel.basic_publish(exchange='', routing_key=self.remove_trip_queue, body=message)
