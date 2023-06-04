package pl.edu.pg.gateway.hotel.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
public class GetHotelDetailsEvent {
    private Long id;
}
