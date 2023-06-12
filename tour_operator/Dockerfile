FROM python:3.11
ENV PYTHONUNBUFFERED=1

ENV ADD_HOTEL_QUEUE=AddHotelQueue
ENV ADD_FLIGHT_QUEUE=AddFlightQueue
ENV GET_HOTELS_QUEUE=GetHotelsQueue
ENV GET_FLIGHTS_QUEUE=GetFlightsQueue
ENV MIN_WAIT_TIME=3
ENV MAX_WAIT_TIME=8

ENV AMQP_RECONNECTION_TRIES=15
ENV AMQP_RECONNECTION_DELAY=5

WORKDIR /app
COPY requirements.txt /app/requirements.txt
RUN pip install -r requirements.txt
COPY . /app

CMD python tour_operator.py