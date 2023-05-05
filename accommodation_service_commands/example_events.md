# Hotel
**Add hotel**
```json
{
  "hotelName": "Radisson Hotel",
  "city": "Gdańsk",
  "country": "Poland",
  "stars": 5,
  "photo": "url",
  "airport": "Paris (CDG)", 
  "description": "Some description",
  "food": "none|breakfast|two-dishes|three-dishes|all-inclusive",
  "rooms": [
    {
      "capacity": 4, 
      "name": "Pokój 4-osobowy", 
      "features": "klimatyzacja|TV|telefon", 
      "numberOfRooms": 20,
      "price": 256.49
    },
    {
      "capacity": 2,
      "name": "Pokój 2-osobowy",
      "features": "klimatyzacja|telefon",
      "numberOfRooms": 10,
      "price": 149.99
    }
  ]
}
```

**Delete hotel**
```json
{
  "source": "testSource",
  "hotelId": 1
}
```

# Room
**Add room**
```json
{
  "source": "testSource",
  "roomNumber": "A201",
  "hotelId": 2,
  "price": 215.50,
  "capacity": 4
}
```

**Delete room**
```json
{
  "source": "testSource",
  "roomId": 1
}
```

# Reservations
**Reserve room** _Not implemented properly yet_
```json
{
  
}
```

**Confirm reservation**
```json
{
  
}
```

**Cancel reservation**
```json

```