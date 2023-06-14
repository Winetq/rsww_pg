package pl.edu.pg.trip.listener.events.payments;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class PostPaymentResponse {
    public Integer status;
}
