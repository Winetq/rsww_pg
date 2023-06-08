package pl.edu.pg.gateway.hotel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class GetHotelsWithParametersEvent {
    private String country;
    private String city;
}
