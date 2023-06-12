import json


class Flight:
    def __init__(self, flight):
        self.id = None
        if "id" in flight:
            self.id = flight["id"]
        self.departureAirport = flight["departureAirport"]
        self.arrivalAirport = flight["arrivalAirport"]
        self.departureDate = flight["departureDate"]
        self.arrivalDate = flight["arrivalDate"]
        self.travelTime = flight["travelTime"]
        self.placesCount = flight["placesCount"]
        # self.price = flight["price"]
        self.price = 0

    def compare(self, flight_dict):
        flight_dict = json.loads(flight_dict)
        if self.departureAirport != flight_dict["departureAirport"]:
            return False
        if self.arrivalAirport != flight_dict["arrivalAirport"]:
            return False
        if self.departureDate != flight_dict["departureDate"]:
            return False
        if self.arrivalDate != flight_dict["arrivalDate"]:
            return False
        if self.travelTime != flight_dict["travelTime"]:
            return False

        return True
