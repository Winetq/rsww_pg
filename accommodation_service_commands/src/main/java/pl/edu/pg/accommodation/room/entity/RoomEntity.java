package pl.edu.pg.accommodation.room.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
@Entity
@Table(name = "Rooms")
@AllArgsConstructor
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    private int capacity;

    private float price;
    @Column(columnDefinition="text")
    private String name;
    @Column(columnDefinition="text")
    private String features;

    @ManyToOne
    @JoinColumn(name = "hotelId", nullable = false)
    private HotelEntity hotel;

    @Override
    public String toString() {
        return "RoomEntity{" +
                "id=" + id +
                ", capacity=" + capacity +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", hotel=" + hotel +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RoomEntity that = (RoomEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
