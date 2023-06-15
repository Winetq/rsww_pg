import os
from datetime import datetime
import pytz


class TOLatestUpdates:
    def __init__(self):
        self.latest_updates_number = int(os.environ.get('LATEST_UPDATES_NUMBER', 10))
        self.timezone = pytz.timezone("Europe/Warsaw")
        self.latest_updates = []

    def add_update(self, event):
        current_time = datetime.now(self.timezone).strftime("%d.%m.%Y %H:%M:%S")
        new_update = {
            "time": current_time,
            "event": event
        }
        self.latest_updates.reverse()
        self.latest_updates.append(new_update)
        # self.latest_updates = sorted(self.latest_updates, key=lambda update: update["time"])
        if len(self.latest_updates) > self.latest_updates_number:
            self.latest_updates = self.latest_updates[-10:]
        self.latest_updates.reverse()

        print("Latest updates number: " + str(len(self.latest_updates)))
