package pl.edu.pg.accommodation.room.listener.event;

import pl.edu.pg.accommodation.event.GenericEvent;

import java.util.Objects;

public class DeleteRoomEvent extends GenericEvent {
    private Long roomId;

    public DeleteRoomEvent() {
        super();
    }

    public DeleteRoomEvent(String source, Long roomId) {
        super(source);
        this.roomId = roomId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DeleteRoomEvent that = (DeleteRoomEvent) o;
        return Objects.equals(roomId, that.roomId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roomId);
    }

    @Override
    public String toString() {
        return "DeleteRoomEvent{" +
                "roomId=" + roomId +
                '}';
    }
}
