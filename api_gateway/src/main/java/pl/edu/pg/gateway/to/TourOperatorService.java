package pl.edu.pg.gateway.to;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.pg.gateway.to.dto.TOUpdateQuery;
import pl.edu.pg.gateway.to.dto.TOUpdateResponse;

import java.util.List;

@Service
class TourOperatorService {
    private final RabbitTemplate rabbitTemplate;
    private static final String GET_TO_LATEST_UPDATES_QUEUE = "GetTOLatestUpdatesQueue";

    @Autowired
    TourOperatorService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    ResponseEntity<List<TOUpdateResponse>> getTOLatestUpdates() {
        List<TOUpdateResponse> response = rabbitTemplate.convertSendAndReceiveAsType(
                GET_TO_LATEST_UPDATES_QUEUE,
                new TOUpdateQuery(),
                new ParameterizedTypeReference<>() {
                });
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
