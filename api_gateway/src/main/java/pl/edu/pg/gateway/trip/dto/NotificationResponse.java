package pl.edu.pg.gateway.trip.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class NotificationResponse {
    private String notification;
}
