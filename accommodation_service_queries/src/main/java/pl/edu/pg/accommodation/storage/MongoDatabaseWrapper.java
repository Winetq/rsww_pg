package pl.edu.pg.accommodation.storage;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

public class MongoDatabaseWrapper {

    private final MongoClient client;
    private final String databaseName;

    private final CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
            MongoClientSettings.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(
                    PojoCodecProvider.builder()
                            .automatic(true)
                            .build()
            )
    );

    public MongoDatabaseWrapper(final MongoClient client,
                                final String databaseName) {
        this.client = client;
        this.databaseName = databaseName;
    }

    public MongoDatabase getDatabase() {
        return client.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);
    }

    public void close() {
        client.close();
    }
}
