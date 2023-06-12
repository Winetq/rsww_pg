import random
import json
import datetime


class HotelTemplate:
    def __init__(self, name, country, city, stars, description, food, photo, rooms, airport):
        self.name = name
        self.country = country
        self.city = city
        self.stars = stars
        self.description = description
        self.food = food
        self.photo = photo
        self.rooms = rooms
        self.airport = airport

    def save(self):
        hotel = {
            "name": self.name,
            "country": self.country,
            "city": self.city,
            "stars": self.stars,
            "description": self.description,
            "food": self.food,
            "photo": self.photo,
            "rooms": self.rooms_to_json(),
            "airport": self.airport
        }

        with open('data/hotels.json', 'a') as outfile:
            outfile.write(json.dumps(hotel) + "\n")

    def rooms_to_json(self):
        rooms_result = [0] * len(self.rooms)
        for i in range(len(self.rooms)):
            room = self.rooms[i]
            room_dict = dict()
            room_dict["name"] = room.name
            room_dict["features"] = room.features
            room_dict["capacity"] = room.capacity
            room_dict["numberOfRooms"] = room.numberOfRooms
            room_dict["basePrice"] = room.basePrice
            rooms_result[i] = room_dict
        return rooms_result


class RoomTemplate:
    def __init__(self, name, features, capacity, numberOfRooms, basePrice):
        self.name = name
        self.features = features
        self.capacity = capacity
        self.numberOfRooms = numberOfRooms
        self.basePrice = basePrice


class FlightTemplate:
    def __init__(self, departure_airport, arrival_airport, date, departure_time, arrival_time, travel_time):
        self.departure_airport = departure_airport
        self.arrival_airport = arrival_airport
        self.departure_date, self.arrival_date = self.getDates(departure_time, arrival_time, date)
        travel_hour = travel_time.split("h")
        travel_min = travel_hour[1].split("min")
        self.travel_time = int(travel_time[0]) * 60 + int(travel_min[0])
        self.placesCount = 150
        self.price = random.randrange(start=100, stop=600)

    def getDates(self, departure_time, arrival_time, date):
        dateDate = date.split(".")
        depTime = departure_time.split(":")
        departure_date = \
            datetime.datetime(int(dateDate[2]), int(dateDate[1]), int(dateDate[0]), int(depTime[0]), int(depTime[1]))
        arrTime = arrival_time.split(":")
        arrival_date = \
            datetime.datetime(int(dateDate[2]), int(dateDate[1]), int(dateDate[0]), int(arrTime[0]), int(arrTime[1]))
        if arrival_date < departure_date:
            arrival_date += datetime.timedelta(days=1)
        departure_date = departure_date.strftime("%d.%m.%Y %H:%M")
        arrival_date = arrival_date.strftime("%d.%m.%Y %H:%M")
        return departure_date, arrival_date

    def save(self):
        flight = {
            "departureAirport": self.departure_airport,
            "arrivalAirport": self.arrival_airport,
            "departureDate": self.departure_date,
            "arrivalDate": self.arrival_date,
            "travelTime": self.travel_time,
            "placesCount": self.placesCount,
            "price": self.price
        }

        with open('data/flights.json', 'a') as outfile:
            outfile.write(json.dumps(flight, default=str) + "\n")
