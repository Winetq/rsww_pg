package pl.edu.pg.trip.enity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.edu.pg.trip.listener.events.HotelDetailsResponse;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    private long id;
    private String place;
    private int stars;
    private String description;
    private String photo;
    private Set<HotelDetailsResponse.Room> rooms;
    private String airport;
    private String food;
    private String name;
}
