package pl.edu.pg.accommodation.ping;

import java.util.Objects;

public class PingEvent {
    private String body;

    public PingEvent() {

    }

    public PingEvent(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PingEvent pingEvent = (PingEvent) o;
        return Objects.equals(body, pingEvent.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(body);
    }

    @Override
    public String toString() {
        return "PingEvent{" +
                "body='" + body + '\'' +
                '}';
    }
}
