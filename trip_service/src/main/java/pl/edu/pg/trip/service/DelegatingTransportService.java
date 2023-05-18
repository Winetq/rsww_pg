package pl.edu.pg.trip.service;

import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.enity.Transport;

import java.util.List;

@Component
public class DelegatingTransportService {
    private final Logger logger = LoggerFactory.getLogger(DelegatingTransportService.class);

    private final AsyncRabbitTemplate template;

    @Autowired
    public DelegatingTransportService(final AsyncRabbitTemplate asyncRabbitTemplate) {
        this.template = asyncRabbitTemplate;
    }

    public List<Transport> getTransports() {
        return ImmutableList.of();
    }
}
