package pl.edu.pg.trip.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.listener.events.NotifyHotelAdded;
import pl.edu.pg.trip.service.TripService;

@Component
public class AccommodationEventListener {

    private final static Logger log = LoggerFactory.getLogger(AccommodationEventListener.class);
    private final TripService tripService;

    @Autowired
    public AccommodationEventListener(final TripService tripService) {
        this.tripService = tripService;
    }

    @RabbitListener(queues = "#{autoDeleteQueue}")
    public void listenOnAccommodationUpdates(NotifyHotelAdded message) {
        log.info("{}", message);
    }
}
