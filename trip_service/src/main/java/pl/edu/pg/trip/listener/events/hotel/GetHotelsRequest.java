package pl.edu.pg.trip.listener.events.hotel;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder(access = AccessLevel.PUBLIC)
@Jacksonized
public class GetHotelsRequest {
    private String destination;
    private Integer adults;
    private Integer people3To9;
    private Integer people10To17;
}
