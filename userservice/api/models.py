from enum import Enum

from django.db import models
from django.contrib.auth.models import AbstractBaseUser, PermissionsMixin

from api.managers import CustomUserManager
from api.roles import CustomUserRoles


class CustomUser(AbstractBaseUser, PermissionsMixin):
    """ Customized application user definition with defined
    additional fields contains personal user data

    Notes
    -----
    Because of inheritance from PermissionsMixin CustomUser model contains
    fields that could be used to authentication and authorization such a groups or permissions.
    """
    USERNAME_MAX_LEN = 100
    FIRSTNAME_MAX_LEN = 100
    LASTNAME_MAX_LEN = 100

    ROLE_CHOICES = (
        (CustomUserRoles.ADMIN, 'Admin'),
        (CustomUserRoles.USER, 'User')
    )

    username = models.CharField(max_length=USERNAME_MAX_LEN, unique=True, blank=False, null=False, verbose_name='Username')
    firstname = models.CharField(max_length=FIRSTNAME_MAX_LEN, null=False, blank=False, verbose_name='Firstname')
    lastname = models.CharField(max_length=LASTNAME_MAX_LEN, null=False, blank=False, verbose_name='Lastname')
    role = models.PositiveSmallIntegerField(choices=ROLE_CHOICES, blank=True, null=True)
    is_staff = models.BooleanField(default=False)
    is_active = models.BooleanField(default=True)

    objects = CustomUserManager()

    USERNAME_FIELD = 'username'
    REQUIRED_FIELDS = ('firstname', 'lastname')

    def __repr__(self):
        return f'CustomUser({self.username!r}, {self.firstname!r}, {self.lastname!r})'

    def __str__(self):
        return f'{self.firstname} {self.lastname}'
