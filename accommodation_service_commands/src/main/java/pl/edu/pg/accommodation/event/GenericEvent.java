package pl.edu.pg.accommodation.event;

import java.util.Objects;

public class GenericEvent {
    private String source;

    public GenericEvent() {

    }

    public GenericEvent(final String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenericEvent that = (GenericEvent) o;
        return Objects.equals(source, that.source);
    }

    @Override
    public int hashCode() {
        return Objects.hash(source);
    }

    @Override
    public String toString() {
        return "GenericEvent{" +
                "source='" + source + '\'' +
                '}';
    }
}
