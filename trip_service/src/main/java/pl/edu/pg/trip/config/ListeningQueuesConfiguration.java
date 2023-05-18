package pl.edu.pg.trip.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class ListeningQueuesConfiguration {
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

    @Bean
    public Queue getHotelsQueue(@Value("${spring.rabbitmq.queue.hotel.get.all}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue getHotelDetailsQueue(@Value("${spring.rabbitmq.queue.hotel.get.single}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean(name = "autoDeleteQueue")
    Queue autoDeleteQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Queue getTripsQueue(@Value("${spring.rabbitmq.queue.trip.get.all}") final String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue getTripDetailsQueue(@Value("${spring.rabbitmq.queue.trip.get.details}") final String queueName) {
        return new Queue(queueName, true);
    }
}
