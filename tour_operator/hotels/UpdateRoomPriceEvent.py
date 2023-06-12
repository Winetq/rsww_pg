import json
import os


class UpdateRoomPriceEvent:
    def __init__(self, channel, hotelId, room_to_update, new_price):
        self.update_room_price_queue = os.environ.get('UPDATE_ROOM_PRICE_QUEUE', 'UpdateRoomPriceQueue')

        update_room_price_event = {
            "hotelId": hotelId,
            "room": {
                "capacity": room_to_update.capacity,
                "name": room_to_update.name,
                "features": room_to_update.features
            },
            "newPrice": new_price
        }
        message = json.loads(json.dumps(update_room_price_event))
        channel.basic_publish(exchange='', routing_key=self.update_room_price_queue, body=message)
