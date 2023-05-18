#!/usr/bin/env bash

jar xf trip_service.jar BOOT-INF/classes/database.properties
sed -i "s/MONGO_USER/$MONGO_USER/" BOOT-INF/classes/database.properties
sed -i "s/MONGO_USER_PWD/$MONGO_USER_PWD/" BOOT-INF/classes/database.properties
sed -i "s/MONGO_HOST/$MONGO_HOST/" BOOT-INF/classes/database.properties
sed -i "s/MONGO_PORT/$MONGO_PORT/" BOOT-INF/classes/database.properties
sed -i "s/DATABASE_NAME/$DATABASE_NAME/" BOOT-INF/classes/database.properties
jar uf trip_service.jar BOOT-INF/classes/database.properties

jar xf trip_service.jar BOOT-INF/classes/application.properties
sed -i "s/RABBIT_HOST/$RABBIT_HOST/" BOOT-INF/classes/application.properties
sed -i "s/RABBIT_USR/$RABBIT_USR/" BOOT-INF/classes/application.properties
sed -i "s/RABBIT_PWD/$RABBIT_PWD/" BOOT-INF/classes/application.properties
sed -i "s/RABBIT_PORT/$RABBIT_PORT/" BOOT-INF/classes/application.properties
jar uf trip_service.jar BOOT-INF/classes/application.properties

java -jar trip_service.jar
