package pl.edu.pg.accommodation.room.listener.event;

import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import pl.edu.pg.accommodation.event.Event;

@Data
@Builder
@Jacksonized
public class DeleteRoomEvent implements Event {
    private Long roomId;
}
