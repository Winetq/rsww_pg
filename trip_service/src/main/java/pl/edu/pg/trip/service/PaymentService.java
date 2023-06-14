package pl.edu.pg.trip.service;

import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.listener.events.payments.PostPaymentResponse;

@Component
public class PaymentService {
    private final AsyncRabbitTemplate template;

    @Autowired
    public PaymentService(final AsyncRabbitTemplate template) {
        this.template = template;
    }

    public PostPaymentResponse paymentRequest() {
        return PostPaymentResponse.builder().build();
    }
}
