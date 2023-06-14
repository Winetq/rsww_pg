import json
import os


class UpdateFlightPriceEvent:
    def __init__(self, channel, flight, new_price):
        self.update_flight_price_queue = os.environ.get('UPDATE_FLIGHT_PRICE_QUEUE', 'UpdateFlightPriceQueue')
        flight_update_event = {
            "flightId": flight.id,
            "price": new_price
        }
        message = json.dumps(flight_update_event)
        channel.basic_publish(exchange='', routing_key=self.update_flight_price_queue, body=message)
