import json
import time
import pika

SLEEP_INTERVAL = 0.1
TIMEOUT = 0.5


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
            return json.loads(body)
        time.sleep(SLEEP_INTERVAL)
        slept += SLEEP_INTERVAL

    return None
