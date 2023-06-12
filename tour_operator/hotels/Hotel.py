import json


class Hotel:
    def __init__(self, hotel):
        self.id = None
        hotel = json.loads(hotel)
        if "id" in hotel:
            self.id = hotel["id"]
        self.name = hotel["name"]
        self.stars = hotel["stars"]
        self.place = hotel["place"]
        self.photo = hotel["photo"]
        self.airport = hotel["airport"]

    def compare(self, hotel):
        """Compares Hotel object with HotelDetails object"""
        if self.name != hotel.name:
            return False
        if self.stars != hotel.stars:
            return False
        if self.photo != hotel.photo:
            return False
        if self.airport != hotel.airport:
            return False

        return True
