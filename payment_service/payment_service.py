import json
import os
import time
import pika
import random
from http import HTTPStatus


class AMQPConsumer:
    def __init__(self):
        self.address = os.environ.get('AMQP_URL', '127.0.0.1')
        self.payment_queue = os.environ.get('PAYMENT_QUEUE', 'PaymentQueue')
        self.reconnection_tries = os.environ.get('AMQP_RECONNECTION_TRIES', 10)
        self.reconnection_delay = os.environ.get('AMQP_RECONNECTION_DELAY', 5)
        self.rejection_probability = int(os.environ.get('REJECTION_PROBABILITY', 10))
        self.connection_tries = 0
        self.connection = None
        self.channel = None
        self.make_connection()

    def make_connection(self):
        try:
            self.connection_tries += 1
            params = pika.ConnectionParameters(self.address)
            self.connection = pika.BlockingConnection(params)
        except pika.exceptions.AMQPConnectionError:
            if self.connection_tries < self.reconnection_tries:
                print(f'Could not connect to AMQP server. Trying again after {self.reconnection_delay} seconds')
                time.sleep(self.reconnection_delay)
                self.make_connection()
            else:
                raise ConnectionError('AMQP server is unreachable. Max reconnection tries have been reached')

    def receive_msg(self, ch, method, properties, body):
        time.sleep(1)
        message = {
            "status": HTTPStatus.ACCEPTED
        }
        if random.randrange(100) < self.rejection_probability:
            message = {
                "status": HTTPStatus.NOT_ACCEPTABLE
            }
        self.channel.basic_publish(exchange='', routing_key=properties.reply_to, body=json.dumps(message))

    def listen(self):
        self.channel = self.connection.channel()
        self.channel.queue_declare(queue=self.payment_queue, durable=True)
        self.channel.basic_consume(queue=self.payment_queue, on_message_callback=self.receive_msg)
        self.channel.start_consuming()

    def close_connection(self):
        self.channel.close()
        self.connection.close()


if __name__ == "__main__":
    consumer = AMQPConsumer()
    consumer.listen()
    consumer.close_connection()
