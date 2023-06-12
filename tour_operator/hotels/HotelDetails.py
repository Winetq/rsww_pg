import json

from hotels.Room import Room


class HotelDetails:
    def __init__(self, hotel):
        self.id = None
        hotel = json.loads(hotel)
        if "id" in hotel:
            self.id = hotel["id"]
        self.name = hotel["name"]
        self.stars = hotel["stars"]
        self.country = hotel["country"]
        self.city = hotel["city"]
        self.description = hotel["description"]
        self.photo = hotel["photo"]
        self.rooms = hotel["rooms"]
        self.airport = hotel["airport"]
        self.food = hotel["food"]

    def getRooms(self):
        self.rooms = [Room(room) for room in self.rooms]
