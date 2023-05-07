package pl.edu.pg.transport.dto;

import pl.edu.pg.transport.entity.Flight;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GetFlightsResponse {

    public static Function<List<Flight>, List<GetFlightDetailsResponse>> entityToDtoMapper() {
        return flights -> flights
                .stream()
                .map(flight -> GetFlightDetailsResponse.entityToDtoMapper().apply(flight))
                .collect(Collectors.toList());
    }

}
