import json

from rest_framework import status
from rest_framework_simplejwt.serializers import TokenRefreshSerializer
from rest_framework_simplejwt.exceptions import TokenError
from rest_framework_simplejwt.serializers import TokenVerifySerializer

from django.conf import settings
from django.contrib.auth import authenticate

from api.models import CustomUser
from api.amqp import AMQPBasicConsumer, RegisterAMQPConsumer
from api.serializers import CustomUserSerializer, CustomTokenObtainPairSerializer


@RegisterAMQPConsumer
class TokenPairAMQPConsumer(AMQPBasicConsumer):
    """ Consumer of messages for getting pair of tokens needed
    by users for proper authentication.

    NOTES
    -----
    Consumer assumes that proper message body contains 'username' and 'password' fields
    """
    queue_name = settings.AMQP.get('POST_TOKEN_PAIR_QUEUE', 'PostTokenPair')
    routing_key = settings.AMQP.get('POST_TOKEN_PAIR_ROUTING_KEY', 'PostTokenPair')
    required_fields = ('username', 'password')

    def on_message_callback(self, ch, method, properties, body) -> None:
        user = self.authenticate_user(body.get('username', ''), body.get('password', ''))
        if not user:
            return self.reply(ch, method, properties, body=json.dumps({'status': status.HTTP_400_BAD_REQUEST}))

        refresh = CustomTokenObtainPairSerializer.get_token(user)
        self.reply(ch, method, properties, body=json.dumps({
            'access': str(refresh.access_token),
            'refresh': str(refresh)
        }).encode('utf-8'))

    def authenticate_user(self, username, password):
        user = authenticate(username=username, password=password)
        if user is None:
            return False
        return user


@RegisterAMQPConsumer
class TokenRefreshAMQPConsumer(AMQPBasicConsumer):
    """ Consumer of messages for refreshing user
    authentication token

    NOTES
    -----
    Consumer assumes that proper message body contains 'refresh' field
    """
    queue_name = settings.AMQP.get('POST_TOKEN_REFRESH_QUEUE', 'PostTokenRefresh')
    routing_key = settings.AMQP.get('POST_TOKEN_REFRESH_ROUTING_KEY', 'PostTokenRefresh')
    required_fields = ('refresh',)

    def on_message_callback(self, ch, method, properties, body) -> None:
        serializer = TokenRefreshSerializer(data=body)
        try:
            serializer.is_valid(raise_exception=True)
        except TokenError as e:
            return self.reply(ch, method, properties, body=json.dumps({'status': status.HTTP_400_BAD_REQUEST}))

        self.reply(ch, method, properties, body=json.dumps({
            'status': status.HTTP_200_OK,
            **serializer.validated_data
        }).encode('utf-8'))


@RegisterAMQPConsumer
class TokenVerifyAMQPConsumer(AMQPBasicConsumer):
    """ Consumer of messages for authenticating user
    operations and user's token validity

    NOTES
    -----
    Consumer assumes that proper message body contains 'token' field
    """
    queue_name = settings.AMQP.get('POST_TOKEN_VERIFY_QUEUE', 'PostTokenVerify')
    routing_key = settings.AMQP.get('POST_TOKEN_VERIFY_ROUTING_KEY', 'PostTokenVerify')
    required_fields = ('token',)

    def on_message_callback(self, ch, method, properties, body) -> None:
        serializer = TokenVerifySerializer(data=body)
        try:
            serializer.is_valid(raise_exception=True)
            self.reply(ch, method, properties, body=json.dumps({'status': status.HTTP_200_OK}))
        except TokenError as e:
            self.reply(ch, method, properties, body=json.dumps({'status': status.HTTP_401_UNAUTHORIZED}))


@RegisterAMQPConsumer
class CustomUsersAMQPConsumer(AMQPBasicConsumer):
    """ Test consumer of messages for getting list of users accounts.
    Could be used to check proper communication with AMQP service
    """
    queue_name = settings.AMQP.get('GET_USERS_QUEUE', 'GetUsers')
    routing_key = settings.AMQP.get('GET_USERS_ROUTING_KEY', 'GetUsers')

    def on_message_callback(self, ch, method, properties, body) -> None:
        users = CustomUser.objects.all()
        self.reply(ch, method, properties, body=json.dumps({
            'users': CustomUserSerializer(users, many=True).data,
            'status': status.HTTP_200_OK
        }).encode('utf-8'))
