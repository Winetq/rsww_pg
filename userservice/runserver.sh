#!/bin/bash

# Make models migrations
echo "Make models migrations"
python manage.py makemigrations

# Apply database migrations
echo "Apply database migrations"
python manage.py migrate

# Init database
echo "Init database models"
python manage.py loaddata dbinit.json

# Launch server
echo "Launch server"
python manage.py runserver 0.0.0.0:8000