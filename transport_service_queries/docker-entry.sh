#!/usr/bin/env bash

jar xf transport_queries.jar BOOT-INF/classes/application.properties
sed -i "s/MONGO_USER/$MONGO_USER/" BOOT-INF/classes/application.properties
sed -i "s/MONGO_PWD/$MONGO_PWD/" BOOT-INF/classes/application.properties
sed -i "s/MONGO_HOST/$MONGO_HOST/" BOOT-INF/classes/application.properties
sed -i "s/MONGO_PORT/$MONGO_PORT/" BOOT-INF/classes/application.properties
sed -i "s/MONGO_DATABASE/$MONGO_DATABASE/" BOOT-INF/classes/application.properties
sed -i "s/RABBIT_HOST/$RABBIT_HOST/" BOOT-INF/classes/application.properties
sed -i "s/RABBIT_USR/$RABBIT_USR/" BOOT-INF/classes/application.properties
sed -i "s/RABBIT_PWD/$RABBIT_PWD/" BOOT-INF/classes/application.properties
sed -i "s/RABBIT_PORT/$RABBIT_PORT/" BOOT-INF/classes/application.properties
jar uf transport_queries.jar BOOT-INF/classes/application.properties

java -jar transport_queries.jar
