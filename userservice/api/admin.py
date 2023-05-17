from django.contrib import admin

from api.models import CustomUser


class CustomUserAdmin(admin.ModelAdmin):
    fieldsets = (
        ('About', {'fields': ('username', 'password', 'firstname', 'lastname')}),
    )


admin.site.register(CustomUser, CustomUserAdmin)
