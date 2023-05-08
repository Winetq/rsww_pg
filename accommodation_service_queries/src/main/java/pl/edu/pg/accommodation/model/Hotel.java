package pl.edu.pg.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    private Long id;
    private String name;
    private String country;
    private String city;
    private Integer stars;
    private String description;
    private String photo;
    private String airport;
    private String food;
    private Set<Room> rooms;
}
