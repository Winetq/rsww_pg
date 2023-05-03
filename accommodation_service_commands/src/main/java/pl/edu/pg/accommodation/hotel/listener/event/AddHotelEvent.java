package pl.edu.pg.accommodation.hotel.listener.event;

import pl.edu.pg.accommodation.event.GenericEvent;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;

import java.util.Objects;
import java.util.function.Function;

public class AddHotelEvent extends GenericEvent {

    private String hotelName;
    private String city;
    private String country;
    private int stars;

    public AddHotelEvent() {
        super();
    }

    public AddHotelEvent(String source, String hotelName, String city, String country, int stars) {
        super(source);
        this.hotelName = hotelName;
        this.city = city;
        this.country = country;
        this.stars = stars;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AddHotelEvent that = (AddHotelEvent) o;
        return stars == that.stars && Objects.equals(hotelName, that.hotelName) && Objects.equals(city, that.city) && Objects.equals(country, that.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), hotelName, city, country, stars);
    }

    @Override
    public String toString() {
        return "AddHotelEvent{" +
                "hotelName='" + hotelName + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", stars=" + stars +
                '}';
    }

    public static Function<AddHotelEvent, HotelEntity> toEntityMapper() {
        return (event) -> {
            final var entity = new HotelEntity();
            entity.setName(event.getHotelName());
            entity.setCity(event.getCity());
            entity.setCountry(event.getCountry());
            entity.setStars(event.getStars());
            return entity;
        };
    }
}
