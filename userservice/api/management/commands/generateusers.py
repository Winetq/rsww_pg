import random
from typing import SupportsInt

from django.core.management.base import BaseCommand

from api.models import CustomUser
from api.roles import CustomUserRoles


class Command(BaseCommand):
    help = 'Generates specified number of application users'

    firstnames = ('Leszek', 'Zygfryd', 'Walenty', 'Herbert', 'Celestyn', 'Wilhelm', 'Wit', 'Michał', 'Zenobiusz',
                  'Hubert', 'Luiza', 'Aniela', 'Antonina', 'Maryla', 'Zenobia', 'Jolanta', 'Regina', 'Hanna',
                  'Melania', 'Rozalia', 'Iwan', 'Jan', 'Kazimierz', 'Zygmunt', 'Jarosław', 'Marcel', 'Tomasz',
                  'Dionizy', 'Korneliusz', 'Arleta', 'Wiesława', 'Kinga', 'Lucyna', 'Wisława', 'Wiara', 'Julita',
                  'Malina', 'Stefania', 'Ludwika')

    lastnames = ('Lech', 'Prusak', 'Moder', 'Gibas', 'Bonar', 'Sieja', 'Strachota', 'Bisek', 'Prokop', 'Dominiak',
                 'Kras', 'Zdroik', 'Jakubik', 'Zagata', 'Bieszczad', 'Bartoszek', 'Bembenek', 'Kurzawa', 'Szcześniak',
                 'Janos', 'Kalata', 'Poręba', 'Biela', 'Gulas', 'Garbacz', 'Urbanowicz')

    def add_arguments(self, parser):
        parser.add_argument(
            '-c',
            '--count',
            nargs='?',
            type=int,
            required=True,
            help='Number of users that should be generated'
        )

    def handle(self, *args, **options):
        count = options.get('count')
        try:
            self._generate_users(count)
            self.stdout.write(self.style.SUCCESS(
                f'Successfully generated {count} users'
            ))
        except Exception as e:
            self.stdout.write(self.style.ERROR(
                f'Exception has occurred during user generation: {e!r}'
            ))

    def _generate_users(self, count: SupportsInt):
        created_count, index = 0, 0

        curr_usernames, curr_firstnames, curr_lastnames = list(), list(), list()
        if len(CustomUser.objects.all()) > 0:
            curr_usernames, curr_firstnames, curr_lastnames = list(zip(
                *CustomUser.objects.all().values_list('username', 'firstname', 'lastname')
            ))

        while created_count < int(count):
            username = f'user{index}'
            index += 1
            if username in curr_usernames:
                continue

            firstname, lastname = random.choice(self.firstnames), random.choice(self.lastnames)
            cu = CustomUser.objects.create_user(username=username, password=username,
                                                firstname=firstname, lastname=lastname,
                                                role=CustomUserRoles.USER)
            self.stdout.write(self.style.SUCCESS(
                f'Successfully created new {cu!r} model object'
            ))
            created_count += 1
