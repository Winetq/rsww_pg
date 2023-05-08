package pl.edu.pg.accommodation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    private long id;
    private String name;
    private int capacity;
    private String features;
    private float price;
}
