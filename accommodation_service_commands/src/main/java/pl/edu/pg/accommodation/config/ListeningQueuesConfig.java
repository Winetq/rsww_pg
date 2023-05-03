package pl.edu.pg.accommodation.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ListeningQueuesConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocations(
                new ClassPathResource("application.properties"),
                new ClassPathResource("queues.properties")
        );
        return configurer;
    }

    @Bean(name = "reserveHotelQueue")
    Queue reserveHotelQueue(@Value("${spring.rabbitmq.queue.hotel.reservation}") final String reserveHotelQueue) {
        return new Queue(reserveHotelQueue, true);
    }

    @Bean(name = "cancelHotelReservationQueue")
    Queue cancelHotelReservationQueue(@Value("${spring.rabbitmq.queue.hotel.reservation.cancel}") final String cancelHotelReservationQueue) {
        return new Queue(cancelHotelReservationQueue, true);
    }

    @Bean(name = "confirmHotelReservationQueue")
    Queue confirmHotelReservationQueue(@Value("${spring.rabbitmq.queue.hotel.reservation.confirm}") final String confirmHotelReservationQueue) {
        return new Queue(confirmHotelReservationQueue, true);
    }

    @Bean(name = "addHotelQueue")
    Queue addHotelQueue(@Value("${spring.rabbitmq.queue.hotel.add}") final String addHotelQueue) {
        return new Queue(addHotelQueue, true);
    }

    @Bean(name = "deleteHotelQueue")
    Queue deleteHotelQueue(@Value("${spring.rabbitmq.queue.hotel.delete}") final String deleteHotelQueue) {
        return new Queue(deleteHotelQueue, true);
    }

    @Bean(name = "addRoomQueue")
    Queue addRoomQueue(@Value("${spring.rabbitmq.queue.hotel.room.add}") final String addRoomQueue) {
        return new Queue(addRoomQueue, true);
    }

    @Bean(name = "deleteRoomQueue")
    Queue deleteRoomQueue(@Value("${spring.rabbitmq.queue.hotel.room.delete}") final String deleteRoomQueue) {
        return new Queue(deleteRoomQueue, true);
    }

    @Bean(name="pingQueue")
    Queue pingQueue(@Value("${spring.rabbitmq.ping.queue}") String pingQueue) {
        return new Queue(pingQueue, true);
    }
}
