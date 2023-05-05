package pl.edu.pg.transport.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pg.transport.entity.Transport;

@Repository
public interface TransportRepository extends MongoRepository<Transport, Long> {
}
