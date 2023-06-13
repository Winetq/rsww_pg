package pl.edu.pg.transport.dto;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Builder
@Jacksonized
public class GetDeparturesResponse {
    private List<String> departures;
}
