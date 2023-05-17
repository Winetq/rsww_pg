from typing import Optional

from django.contrib.auth.models import BaseUserManager

from api.roles import CustomUserRoles


class CustomUserManager(BaseUserManager):
    def create_user(self, username: str, password: Optional[str | bytes], firstname: str, lastname: str, **other):
        user = self.model(username=username, firstname=firstname, lastname=lastname, **other)
        user.set_password(password)
        user.save()
        return user

    def create_superuser(self, username: str, password: Optional[str], firstname: str, lastname: str, **other):
        other.setdefault('is_staff', True)
        other.setdefault('is_superuser', True)
        other.setdefault('is_active', True)
        other.setdefault('role', CustomUserRoles.ADMIN)

        if other.get('is_staff', False) is False:
            raise ValueError('Superuser must have field is_stuff equal to True')
        if other.get('is_superuser', False) is False:
            raise ValueError('Superuser must have field is_superuser equal to True')

        return self.create_user(username, password, firstname, lastname, **other)
