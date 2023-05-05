package pl.edu.pg.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.edu.pg.transport.entity.Transport;

@Repository
public interface TransportRepository extends JpaRepository<Transport, Long> {
}
