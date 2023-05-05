package pl.edu.pg.accommodation.ping;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class PingEvent {
    private String body;
}
