package pl.edu.pg.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Value("${spring.rabbitmq.queue.getFlightsQueue}")
    private String getFlightsQueue;

    @Value("${spring.rabbitmq.queue.getFlightsWithParametersQueue}")
    private String getFlightsWithParametersQueue;

    @Value("${spring.rabbitmq.queue.getFlightDetailsQueue}")
    private String getFlightDetailsQueue;

    @Value("${spring.rabbitmq.queue.addFlightNotificationQueue}")
    private String addFlightNotificationQueue;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Bean
    public Queue getFlightsQueue() {
        return new Queue(getFlightsQueue, true);
    }

    @Bean
    public Queue getFlightsWithParametersQueue() {
        return new Queue(getFlightsWithParametersQueue, true);
    }

    @Bean
    public Queue getFlightDetailsQueue() {
        return new Queue(getFlightDetailsQueue, true);
    }

    @Bean
    public Queue addFlightNotificationQueue() {
        return new Queue(addFlightNotificationQueue, true);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        cachingConnectionFactory.setPort(Integer.parseInt(port));
        return cachingConnectionFactory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
