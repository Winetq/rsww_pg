package pl.edu.pg.gateway.transport.dto;

import lombok.Builder;

@Builder
public class GetFlightDetailsQuery {
    private long id;

    public long getId() {
        return id;
    }
}
