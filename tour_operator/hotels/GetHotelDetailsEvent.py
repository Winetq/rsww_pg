import json
import os
from functions import send_message_and_get_response
from hotels.HotelDetails import HotelDetails


class GetHotelDetailsEvent:
    def __init__(self, channel, callback_queue, hotel):
        self.get_hotel_details_queue = os.environ.get('GET_HOTEL_DETAILS_QUEUE', 'GetHotelDetailsQueue')
        # TODO: check if id or hotelId
        mess = {
            "id": hotel.id
        }
        hotel_details = json.loads(send_message_and_get_response(
            channel, self.get_hotel_details_queue, callback_queue, message=json.dumps(mess)))
        self.hotel_details = HotelDetails(hotel_details)
