import json
import time
import pika
from datetime import datetime, timedelta
from trips.AddTripEvent import AddTripEvent

SLEEP_INTERVAL = 0.1
TIMEOUT = 5


def send_message_and_get_response(channel, queue, callback_queue, message=json.dumps({})):
    """
    :param channel:
    :param queue: queue where message is sent
    :param callback_queue: queue where response is located
    :param message: message to sent
    :return: body of message
    """
    channel.basic_publish(
        exchange='',
        routing_key=queue,
        properties=pika.BasicProperties(reply_to=callback_queue),
        body=message
    )
    slept = 0
    while slept < TIMEOUT:
        method_frame, header_frame, body = channel.basic_get(queue=callback_queue)
        if method_frame is not None:
            return body
        time.sleep(SLEEP_INTERVAL)
        slept += SLEEP_INTERVAL

    return None


def check_if_correct_date(departure_date, arrival_date):
    """
    :param departure_date:
    :param arrival_date:
    :return: True if difference between given dates is up to 16 days
    """
    dt1 = datetime.strptime(departure_date, "%d.%m.%Y %H:%M")
    dt2 = datetime.strptime(arrival_date, "%d.%m.%Y %H:%M")
    if dt1 > dt2:
        return False
    return dt2 - dt1 <= timedelta(days=16)


def get_compatible_arrival_flights(dep_flight, arrival_flights):
    """

    :param dep_flight: given flight
    :param arrival_flights: all flights with the same abroad airport
    :return: all arrival flights matching given departure flight based on dates difference and matching airports
    """
    # all arrival flights with two compatible polish airports
    arr_flights = [arrival_flight for arrival_flight in arrival_flights
                   if dep_flight.departureAirport == arrival_flight.arrivalAirport]
    # all arrival flights with 16 days of trip
    arr_flights = [arrival_flight for arrival_flight in arr_flights
                   if check_if_correct_date(dep_flight.departureDate, arrival_flight.arrivalDate)]
    return arr_flights


def get_compatible_departure_flights(departure_flights, arr_flight):
    """

    :param departure_flights: all flights with the same abroad airport
    :param arr_flight: given flight
    :return: all departure flights matching given arrival flight based on dates difference and matching airports
    """
    # all departure flights with two compatible polish airports
    dep_flights = [departure_flight for departure_flight in departure_flights
                   if departure_flight.departureAirport == arr_flight.arrivalAirport]
    # all arrival flights with 16 days of trip
    del_flights = [departure_flight for departure_flight in dep_flights
                   if check_if_correct_date(departure_flight.departureDate, arr_flight.arrivalDate)]
    return del_flights


def add_trip(channel, add_trip_queue, hotel, start_flight, end_flight):
    """
    Adds given trip combination to queue named add_trip_queue
    """
    # add new trip to queue
    message = AddTripEvent(hotel, start_flight, end_flight).getAddTripMessage()
    channel.basic_publish(exchange='', routing_key=add_trip_queue, body=message)


def generate_trips_for_hotel(channel, add_trip_queue, hotel, flights):
    """
    Generates trip combinations based on given hotel
    """
    # all departure flights where arrival airport is abroad
    departure_flights = [flight for flight in flights if flight.arrivalAirport == hotel.airport]
    # all arrival flights where departure airport is abroad
    arrival_flights = [flight for flight in flights if flight.departureAirport == hotel.airport]

    # for all flights with compatible departure airport
    for dep_flight in departure_flights:
        arr_flights = get_compatible_arrival_flights(dep_flight, arrival_flights)
        for arr_flight in arr_flights:
            add_trip(channel, add_trip_queue, hotel, dep_flight, arr_flight)


def generate_trips(channel, add_trip_queue, hotels, flights):
    """
    Adds trips (combinations of given hotels and flights) to queue named add_trip_queue
    """
    print("Generating initial trips")
    for hotel in hotels:
        generate_trips_for_hotel(channel, add_trip_queue, hotel, flights)


def generate_trips_for_flight(channel, add_trip_queue, flight, hotels, flights):
    """
    Generates trip combinations based on given flight
    """
    for hotel in hotels:
        # if flight is connected with hotel
        if flight.arrivalAirport == hotel.airport or flight.departureAirport == hotel.airport:
            # if given flight is departure (arrival airport is abroad)
            if flight.arrivalAirport == hotel.airport:
                # all arrival flights where departure airport is abroad
                arrival_flights = [f for f in flights if f.departureAirport == hotel.airport]
                arr_flights = get_compatible_arrival_flights(flight, arrival_flights)
                for arr_flight in arr_flights:
                    add_trip(channel, add_trip_queue, hotel, flight, arr_flight)
            # if given flight is arrival (departure airport is abroad)
            else:
                # all departure flights where arrival airport is abroad
                departure_flights = [f for f in flights if f.arrivalAirport == hotel.airport]
                dep_flights = get_compatible_departure_flights(departure_flights, flight)
                for dep_flight in dep_flights:
                    add_trip(channel, add_trip_queue, hotel, dep_flight, flight)
