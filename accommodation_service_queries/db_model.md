# Kolekcje
## Kolekcja `hotels`
Kolekcja zawiera dostępne hotele wraz ze szczegółowymi danymi na ich temat. 
Każdy dokument zawiera dane o hotelu, oraz listę zagnieżdżonych dokumentów reprezentujących pokoje dostępne w ramach
danego hotelu.
Przykładowy obiekt
```json
{
  "id": 1,
  "name": "Radisson Hotel",
  "country": "Poland",
  "city": "Gdańsk",
  "stars": 4,
  "rooms": [
    {
      "id": 1,
      "capacity": 4,
      "price": 239.99
    }
  ]
}
```
Obiekty reprezentujące hotele lub pokoje __nie muszą__ zawierać wszystkich danych które znajdują się w modelu w serwisie obsługującym
komendy. Interesują nas elementy ważne z punktu widzenia zapytań.

## Kolekcja `reservations`
Każdy dokument w kolekcji przedstawia pojedyńczą rezerwację, tzn.
- który pokój został zarezerwowany (opcjonalnie podstawowe informacje o pokoju, takie jak np. hotel w którym się znajduje)
- data rozpoczęcia rezerwacji
- data zakończenia rezerwacji
- identyfikator osoby na którą jest rezerwacja
```json
{
  "id": 1,
  "startDate": "06.06.2023",
  "endDate": "21.06.2023",
  "room": {
    "hotelId": 1,
    "roomId": 1,
    "roomNumber": "A211"
  },
  "owner": {
    "id": 1,
    "login": "user1"
  }
}
```