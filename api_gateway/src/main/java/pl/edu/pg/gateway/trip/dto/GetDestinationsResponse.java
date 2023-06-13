package pl.edu.pg.gateway.trip.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class GetDestinationsResponse {
    List<String> destinations;
}
