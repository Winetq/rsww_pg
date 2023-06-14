import json


class GetTOLatestUpdatesEvent:
    def __init__(self, channel, queue, get_to_latest_updates_event):
        get_to_latest_updates_event = json.dumps(get_to_latest_updates_event)
        channel.basic_publish(
            exchange='',
            routing_key=queue,
            body=get_to_latest_updates_event
        )
