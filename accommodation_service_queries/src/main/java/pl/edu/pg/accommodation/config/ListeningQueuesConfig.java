package pl.edu.pg.accommodation.config;

import org.springframework.amqp.core.AnonymousQueue;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListeningQueuesConfig {

    @Bean
    public Queue getHotelsQueue(@Value("${spring.rabbitmq.queue.hotel.get.all}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue getHotelDetailsQueue(@Value("${spring.rabbitmq.queue.hotel.get.single}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue updateAddHotelQueue(@Value("${spring.rabbitmq.queue.update.hotel.add}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue updateAddRoomQueue(@Value("${spring.rabbitmq.queue.update.hotel.room.add}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue updateDeleteHotelQueue(@Value("${spring.rabbitmq.queue.update.hotel.remove}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Queue updateDeleteRoomQueue(@Value("${spring.rabbitmq.queue.update.hotel.room.remove}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean(name = "autoDeleteQueue")
    Queue autoDeleteQueue() {
        return new AnonymousQueue();
    }
}
