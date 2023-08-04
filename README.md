## How to run  

1. Run docker-compose.databases.yml file:
```
docker compose -f docker-compose.databases.yml up -d
```

2. Create databases (transport_commands_db_175132, userservice_db_175132, accommodation_service_commands_db_175132) in 
   PostgreSQL that is running.
   
3. Run docker-compose.services.yml file:
```
docker compose -f docker-compose.services.yml up --build
```

4. If you want to load data, you can run initializer.py file from tour_operator. After that, tour_operator will also 
   start generating example events.

## Architecture Diagram

![ArchitectureDiagram](https://github.com/Winetq/research-project/assets/62242952/03ab21e2-25f8-494c-8fb3-f4e945871cf7)
