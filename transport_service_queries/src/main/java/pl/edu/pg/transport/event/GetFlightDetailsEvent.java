package pl.edu.pg.transport.event;

public class GetFlightDetailsEvent extends GenericEvent {
    private long id;

    public long getId() {
        return id;
    }
}
