class Room:
    def __init__(self, room):
        self.capacity = room["capacity"]
        self.name = room["name"]
        self.features = room["features"]

        self.numberOfRooms = None
        if "numberOfRooms" in room:
            self.numberOfRooms = room["numberOfRooms"]

        self.basePrice = None
        if "basePrice" in room:
            self.basePrice = room["basePrice"]
