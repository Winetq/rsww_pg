import json


class AddTripEvent:
    def __init__(self, hotel, startFlight, endFlight):
        self.hotelId = hotel.id
        self.startFlightId = startFlight.id
        self.endFlightId = endFlight.id

    def getAddTripMessage(self):
        trip = {
            "hotelId": int(self.hotelId),
            "startFlightId": int(self.startFlightId),
            "endFlightId": int(self.endFlightId)
        }
        return json.dumps(trip)
