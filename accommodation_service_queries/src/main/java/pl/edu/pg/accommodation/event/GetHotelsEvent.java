package pl.edu.pg.accommodation.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class GetHotelsEvent implements Event {
    private String destination;
    private Integer adults;
    private Integer people3To9;
    private Integer people10To17;
}
