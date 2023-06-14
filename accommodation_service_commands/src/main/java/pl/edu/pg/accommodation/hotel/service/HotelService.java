package pl.edu.pg.accommodation.hotel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.pg.accommodation.hotel.entity.HotelEntity;
import pl.edu.pg.accommodation.hotel.notifier.HotelEventNotifier;
import pl.edu.pg.accommodation.hotel.repository.HotelRepository;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final HotelEventNotifier notifier;

    @Autowired
    public HotelService(final HotelRepository hotelRepository,
                        final HotelEventNotifier notifier) {
        this.hotelRepository = hotelRepository;
        this.notifier = notifier;
    }

    public HotelEntity addHotel(final HotelEntity hotel) {
        final var newHotel = hotelRepository.save(hotel);
        notifier.notifyNewHotel(newHotel);
        return newHotel;
    }

    public void deleteHotel(final HotelEntity hotel) {
        hotelRepository.delete(hotel);
    }

    public Optional<HotelEntity> findHotelById(final Long hotelId) {
        return hotelRepository.findById(hotelId);
    }

    public Iterable<HotelEntity> findAllHotels() {
        return hotelRepository.findAll();
    }

    public Iterable<HotelEntity> findHotelsByMatchingName(String hotelNameQuery) {
        final var hotels = hotelRepository.findAll();
        return hotels.stream()
                .filter(hotel -> hotel.getName().startsWith(hotelNameQuery))
                .collect(Collectors.toList());
    }
}
