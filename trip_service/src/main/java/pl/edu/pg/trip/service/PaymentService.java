package pl.edu.pg.trip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import pl.edu.pg.trip.listener.events.payments.PostPayment;
import pl.edu.pg.trip.listener.events.payments.PostPaymentResponse;
import pl.edu.pg.trip.listener.events.trip.reservation.transport.CancelFlightReservationCommand;
import pl.edu.pg.trip.listener.events.trip.reservation.transport.ReservationResponse;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
public class PaymentService {
    private final static Logger logger = LoggerFactory.getLogger(PaymentService.class);
    private final AsyncRabbitTemplate template;
    private final String paymentQueueName;

    @Autowired
    public PaymentService(final AsyncRabbitTemplate template,
                          @Value("${spring.rabbitmq.queue.payment.post}") String paymentQueueName) {
        this.template = template;
        this.paymentQueueName = paymentQueueName;
    }

    public PostPaymentResponse paymentRequest() {
//        final var dto = PostPayment.builder().build();
//        final CompletableFuture<PostPaymentResponse> completableRequest = template.convertSendAndReceiveAsType(
//                paymentQueueName,
//                dto,
//                new ParameterizedTypeReference<>() {
//                }
//        );
//
//        try {
//            return completableRequest.get();
//        }  catch (ExecutionException | InterruptedException e) {
//            logger.error("Cannot continue with the payment", e);
//        }
//        return PostPaymentResponse.builder().status(500).build();
        final var random = new Random();
        final var randomValue = random.nextInt() % 10;
        if (randomValue >= 8) {
            return PostPaymentResponse.builder().status(500).build();
        } else {
            return PostPaymentResponse.builder().status(202).build();
        }
    }
}
