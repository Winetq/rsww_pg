import json
import pika
import time
import threading
import logging
from typing import Iterable

from django.conf import settings
from rest_framework import status


class AMQPBasicConsumer(object):
    """ Class of AMQP messages basic consumer without
    extended communication handlers

    Attributes
    ----------
    queue_name : str
        Name of the queue from which consumer should consume messages
    routing_key : str
        Name of routing key of consumed messages
    required_fields : Iterable[str] optional
        Iterable strings of required fields needed for proper working consumer.
        When specified message body is checked before invoking on_message handler
    amqp_url : str
        URL of amqp server which consumer should connect to
    should_reconnect : bool default=True
        Should consumer reconnect after failed connecting
    reconnection_tries : bool default=True
        Max number of tries of reconnections
    reconnection_delay : int default=5
        Seconds of delay between reconnections
    """
    queue_name: str = None
    routing_key: str = None
    required_fields: Iterable[str] = None

    amqp_url: str = settings.AMQP.get('URL')
    should_reconnect: bool = settings.AMQP.get('SHOULD_RECONNECT')
    reconnection_tries: int = settings.AMQP.get('RECONNECTION_TRIES')
    reconnection_delay: int = settings.AMQP.get('RECONNECTION_DELAY')

    def __init__(self):
        self._channel = None
        self._connection = None
        self._consuming = False
        self._conn_params = None
        self.required_fields = self.required_fields if self.required_fields is not None else list()
        self._connection_tries = 0
        self._logger = logging.getLogger(__name__)
        self._validate_state()
        self._config_connection()

    def on_message_callback(self, ch, method, properties, body) -> None:
        """ Method invoked every time message appears
        with specified parameters defined in pika package
        """
        pass

    def reply(self, ch, method, properties, body) -> None:
        """ Reply on received message with provided body
        and pika package parameters appears with received message
        """
        self._logger.info(f'<{type(self).__name__}> has replied for received message')
        ch.basic_publish('', routing_key=properties.reply_to, properties=properties, body=body)

    def start_consuming(self):
        """ Start loop of consuming messages.
        Invoking this method blocks invoking thread
        """
        self._consuming = True
        self._channel.start_consuming()

    def _on_message_precallback(self, ch, method, properties, body):
        self._logger.info(f'<{type(self).__name__}> has received message with body: {body}')
        try:
            body = json.loads(body) if len(body) > 0 else dict()
        except json.JSONDecodeError:
            return self.reply(ch, method, properties, json.dumps({
                'details': 'Could not parse message body',
                'status': status.HTTP_400_BAD_REQUEST
            }))

        for required_field in self.required_fields:
            if body.get(required_field, None) is None:
                return self.reply(ch, method, properties, json.dumps({
                    'details': f'Required field "{required_field}" unprovided',
                    'status': status.HTTP_400_BAD_REQUEST
                }))

        self.on_message_callback(ch, method, properties, body)

    def _config_connection(self):
        self._conn_params = pika.ConnectionParameters(self.amqp_url)
        self._make_connection()
        self._channel = self._connection.channel()
        self._channel.queue_declare(self.queue_name)
        self._channel.basic_consume(self.queue_name, self._on_message_precallback, auto_ack=True)

    def _make_connection(self):
        try:
            self._connection_tries += 1
            self._connection = pika.BlockingConnection(self._conn_params)
        except pika.exceptions.AMQPConnectionError:
            if self._connection_tries < self.reconnection_tries:
                print(f'Could not connect to AMQP server. Trying again after {self.reconnection_delay} seconds')
                time.sleep(self.reconnection_delay)
                self._make_connection()
            else:
                raise ConnectionError('AMQP server is unreachable. Max reconnection tries have been reached')

    def _close_connection(self):
        self._connection.close()

    def _validate_state(self):
        if not isinstance(self.amqp_url, str):
            raise TypeError(f'Invalid type of amqp parameter. Has to be str but was {type(self.amqp_url).__name__}')

        if not isinstance(self.queue_name, str):
            raise AttributeError('Queue name has to be declared for connection')


class RegisterAMQPConsumer(object):
    """ Register class based decorator for registering and launching
    collected consumers after loading of django applications
    """
    consumers: list[AMQPBasicConsumer] = []

    def __init__(self, consumer):
        self.consumer = consumer
        type(self).consumers.append(consumer)

    @classmethod
    def start_consuming(cls):
        """ Start consuming messages in
        all collected consumers
        """
        for consumer in cls.consumers:
            threading.Thread(target=lambda c: c().start_consuming(), daemon=True, args=(consumer,)).start()

    def __call__(self, *args, **kwargs):
        return self.consumer(*args, **kwargs)
