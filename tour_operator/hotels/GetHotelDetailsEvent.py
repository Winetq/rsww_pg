import json
import os
from functions import send_message_and_get_response
from hotels.HotelDetails import HotelDetails


class GetHotelDetailsEvent:
    def __init__(self, channel, callback_queue, hotel):
        self.get_hotel_details_queue = os.environ.get('GET_HOTEL_DETAILS_QUEUE', 'GetHotelDetailsQueue')
        mess = {
            "id": hotel.id
        }
        hotel_details = send_message_and_get_response(
            channel, self.get_hotel_details_queue, callback_queue, message=json.dumps(mess))
        if hotel_details is not None and hotel_details is not []:
            hotel_details = json.loads(hotel_details)
            self.hotel_details = HotelDetails(json.dumps(hotel_details))
        else:
            self.hotel_details = HotelDetails(self.hotel_details)
