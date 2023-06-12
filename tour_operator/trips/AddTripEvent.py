import json


class AddTripEvent:
    def __init__(self, hotel, startFlight, endFlight):
        self.hotelId = hotel.id
        self.startFlightId = startFlight.id
        self.endFlightId = endFlight.id

    def getAddTripMessage(self):
        trip = {
            "hotelId": self.hotelId,
            "startFlightId": self.startFlightId,
            "endFlightId": self.endFlightId
        }
        return json.loads(trip)
