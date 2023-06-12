# Flight

**Add flight**
```json
{
  "departureAirport": "Warsaw (KOD)",
  "arrivalAirport": "Paris (CDG)",
  "departureDate": "16.08.2023 21:20",
  "arrivalDate": "17.08.2023 06:50",
  "travelTime": 105,
  "placesCount": 50,
  "price": 490
}
```

**Reserve flight**
```json
{
  "flightId": 4,
  "userId": 2,
  "numberOfPeople": 10
}
```

**Cancel flight**
```json
{
  "reservationId": 4
}
```

**Confirm flight**
```json
{
  "reservationId": 4
}
```

**Update flight price**
```json
{
  "flightId": 1,
  "price": 490
}
```
