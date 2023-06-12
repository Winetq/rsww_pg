package pl.edu.pg.trip.trip.management.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.service.TripService;
import pl.edu.pg.trip.trip.management.dto.AddTripEvent;
import pl.edu.pg.trip.trip.management.dto.RemoveTripEvent;

@Component
public class TripManagementListener {

    private static final Logger log = LoggerFactory.getLogger(TripManagementListener.class);
    private final TripService tripService;

    @Autowired
    public TripManagementListener(final TripService tripService) {
        this.tripService = tripService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trip.add}")
    public void addTrip(AddTripEvent event) {
        log.debug("Add trip: {}", event);
        final var entity = AddTripEvent.eventToEntityMapper().apply(event);
        tripService.addTrip(entity);
        log.debug("Trip added: {}", entity);
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.trip.remove}")
    public void removeTrip(RemoveTripEvent event) {
        log.debug("Remove trip: {}", event);
        tripService.removeTrip(event.getTripId());
    }
}
