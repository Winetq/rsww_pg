from django.core.management.base import BaseCommand
from django.db.utils import IntegrityError

from api.models import CustomUser


class Command(BaseCommand):
    help = 'Generates super user with provided username and password'

    def add_arguments(self, parser):
        parser.add_argument(
            '-un', '--username',
            nargs='?', type=str, required=False, default='admin',
            help='Username of superuser account'
        )
        parser.add_argument(
            '-pwd', '--password',
            nargs='?', type=str, required=False, default='admin',
            help='Password of superuser account'
        )
        parser.add_argument(
            '-fn', '--firstname',
            nargs='?', type=str, required=False, default='Admin',
            help='Firstname of superuser account'
        )
        parser.add_argument(
            '-ln', '--lastname',
            nargs='?', type=str, required=False, default='Admin',
            help='Lastname of superuser account'
        )

    def handle(self, *args, **options):
        username, password = options.get('username'), options.get('password')
        firstname, lastname = options.get('firstname'), options.get('lastname')

        try:
            cu = CustomUser.objects.create_superuser(username, password, firstname, lastname)
            self.stdout.write(self.style.SUCCESS(
                f'Successfully created new superuser {cu!r}'
            ))
        except IntegrityError as ie:
            self.stdout.write(self.style.ERROR(
                f'Superuser with provided username already exists'
            ))
        except Exception as e:
            self.stdout.write(self.style.ERROR(
                f'Exception has occurred during creating new admin: {e!r}'
            ))


