import json


class GetTOLatestUpdatesEvent:
    def __init__(self, channel, queue, properties, get_to_latest_updates_event):
        get_to_latest_updates_event = json.dumps(get_to_latest_updates_event)
        channel.basic_publish(
            exchange='',
            properties=properties,
            routing_key=queue,
            body=get_to_latest_updates_event
        )
