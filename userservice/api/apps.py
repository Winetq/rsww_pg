import os
from django.apps import AppConfig


class ApiConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'api'

    def ready(self):
        if os.environ.get('RUN_MAIN'):
            from api import views # should be imported before api.amqp
            from api.amqp import RegisterAMQPConsumer
            RegisterAMQPConsumer.start_consuming()
