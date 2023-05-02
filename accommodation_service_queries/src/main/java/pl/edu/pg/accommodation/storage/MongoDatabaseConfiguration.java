package pl.edu.pg.accommodation.storage;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("/database.properties")
public class MongoDatabaseConfiguration {

    @Value("${mongo.connection.url}")
    private String connectionString;
    @Value("${database.name}")
    private String database;

    @Bean(destroyMethod = "close")
    public MongoDatabaseWrapper configureClient() {
        final var mongoClient = MongoClients.create(connectionString);
        return new MongoDatabaseWrapper(mongoClient, database);
    }
}
