FROM python:3.10-slim

RUN apt-get update && \
    apt-get install -y postgresql-client libpq-dev python3-dev

RUN pip install --upgrade pip

# Enviroment variables for proper connection with PostreSQL database
ENV DB_NAME userservice
ENV DB_USER user
ENV DB_PASSWORD password
ENV DB_HOST 127.0.0.1
ENV DB_PORT 5432

# Environment variables for AMQP consumers
ENV AMQP_URL rabbitmq
ENV AMQP_POST_TOKEN_PAIR_QUEUE PostTokenPair
ENV AMQP_POST_TOKEN_PAIR_ROUTING_KEY PostTokenPair
ENV AMQP_POST_TOKEN_REFRESH_QUEUE PostTokenRefresh
ENV AMQP_POST_TOKEN_REFRESH_ROUTING_KEY PostTokenRefresh
ENV AMQP_POST_TOKEN_VERIFY_QUEUE PostTokenVerify
ENV AMQP_POST_TOKEN_VERIFY_ROUTING_KEY PostTokenVerify

WORKDIR /userservice

COPY requirements.txt .

RUN pip install -r requirements.txt

COPY . .

EXPOSE 8000
RUN chmod 755 ./runserver.sh
ENTRYPOINT ["./runserver.sh"]