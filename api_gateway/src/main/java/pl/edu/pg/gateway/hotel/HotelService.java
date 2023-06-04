package pl.edu.pg.gateway.hotel;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.pg.gateway.hotel.dto.GetHotelDetailResponseEvent;
import pl.edu.pg.gateway.hotel.dto.GetHotelDetailsEvent;
import pl.edu.pg.gateway.hotel.dto.GetHotelsEvent;
import pl.edu.pg.gateway.hotel.dto.GetHotelsResponseEvent;

@Service
class HotelService {
    private final RabbitTemplate rabbitTemplate;
    private final String getHotelsQueueName;
    private final String getHotelDetailsQueueName;

    @Autowired
    HotelService(final RabbitTemplate rabbitTemplate,
                 @Value("${spring.rabbitmq.queue.hotel.get.all}") final String getHotelsQueueName,
                 @Value("${spring.rabbitmq.queue.hotel.get.single}") final String getHotelDetailsQueueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.getHotelsQueueName = getHotelsQueueName;
        this.getHotelDetailsQueueName = getHotelDetailsQueueName;
    }

    ResponseEntity<GetHotelDetailResponseEvent> getHotelDetails(Long id) {
        GetHotelDetailsEvent query = GetHotelDetailsEvent.builder().id(id).build();
        GetHotelDetailResponseEvent response = rabbitTemplate.convertSendAndReceiveAsType(
                getHotelDetailsQueueName,
                query,
                new ParameterizedTypeReference<>() {
                });
        if (response != null && response.getId() == -1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    ResponseEntity<GetHotelsResponseEvent> getHotels() {
        GetHotelsEvent query = GetHotelsEvent.builder().build();
        GetHotelsResponseEvent response = rabbitTemplate.convertSendAndReceiveAsType(
                getHotelsQueueName,
                query,
                new ParameterizedTypeReference<>() {
                });
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
