package pl.edu.pg.accommodation.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class RabbitMQConfig {

    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocations(
                new ClassPathResource("application.properties"),
                new ClassPathResource("queues.properties")
        );
        return configurer;
    }
    private final String pingQueue;
    private final String pingNotificationQueue;
    private final String username;
    private final String password;
    private final String host;

    public RabbitMQConfig(@Value("${spring.rabbitmq.ping.queue}") String pingQueue,
                          @Value("${spring.rabbitmq.ping.notification.queue}") String pingNotificationQueue,
                          @Value("${spring.rabbitmq.username}") String username,
                          @Value("${spring.rabbitmq.password}") String password,
                          @Value("${spring.rabbitmq.host}") String host) {

        this.pingQueue = pingQueue;
        this.pingNotificationQueue = pingNotificationQueue;
        this.username = username;
        this.password = password;
        this.host = host;
    }

    @Bean(name="pingQueue")
    Queue pingQueue() {
        return new Queue(pingQueue, true);
    }

    @Bean(name = "pingNotificationQueue")
    Queue pingNotificationQueue() {
        return new Queue(pingNotificationQueue, true);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(host);
        cachingConnectionFactory.setUsername(username);
        cachingConnectionFactory.setPassword(password);
        return cachingConnectionFactory;
    }

    @Bean
    FanoutExchange updateHotelFanout(@Value("${spring.rabbitmq.fanout.hotel.add}") String fanoutName) {
        return new FanoutExchange(fanoutName);
    }

    @Bean
    @Qualifier("autoDeleteQueue")
    public Binding addHotelFanout(FanoutExchange fanout, Queue autoDeleteQueue) {
        return BindingBuilder.bind(autoDeleteQueue).to(fanout);
    }
}
