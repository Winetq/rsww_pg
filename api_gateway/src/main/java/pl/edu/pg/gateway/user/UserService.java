package pl.edu.pg.gateway.user;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.edu.pg.gateway.user.dto.*;

@Service
class UserService {
    private final RabbitTemplate rabbitTemplate;
    private static final String AMQP_POST_TOKEN_PAIR_QUEUE = "PostTokenPair";
    private static final String AMQP_POST_TOKEN_REFRESH_QUEUE  = "PostTokenRefresh";

    @Autowired
    UserService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    ResponseEntity<PostTokenPairResponse> loginUser(UserDto userDto) {
        PostTokenPairResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                AMQP_POST_TOKEN_PAIR_QUEUE,
                userDto,
                new ParameterizedTypeReference<>() {
                });
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    ResponseEntity<PostTokenRefreshResponse> refreshToken(RefreshTokenDto tokenDto) {
        PostTokenRefreshResponse response = rabbitTemplate.convertSendAndReceiveAsType(
                AMQP_POST_TOKEN_REFRESH_QUEUE,
                tokenDto,
                new ParameterizedTypeReference<>() {
                });
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(response != null ? response.status() : 400));
    }

}
