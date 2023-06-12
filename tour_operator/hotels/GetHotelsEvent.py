import json
import os
from hotels.Hotel import Hotel
from functions import send_message_and_get_response


class GetHotelsEvent:
    def __init__(self, channel, callback_queue):
        self.get_hotels_queue = os.environ.get('GET_HOTELS_QUEUE', 'GetHotelsQueue')
        hotels = json.loads(send_message_and_get_response(channel, self.get_hotels_queue, callback_queue))
        if hotels is not None and hotels is not []:
            self.hotels = [Hotel(json.dumps(hotel)) for hotel in hotels["hotels"]]
