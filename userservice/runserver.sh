#!/bin/bash

# Init database
./dbinit.sh

# Launch server
echo "Launch server"
python manage.py runserver 0.0.0.0:8000