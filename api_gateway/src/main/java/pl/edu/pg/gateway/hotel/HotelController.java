package pl.edu.pg.gateway.hotel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pg.gateway.hotel.dto.GetHotelDetailResponseEvent;
import pl.edu.pg.gateway.hotel.dto.GetHotelsResponseEvent;

@RestController
@RequestMapping("api/hotels")
class HotelController {
    private final HotelService hotelService;

    @Autowired
    HotelController(final HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping("{id}")
    ResponseEntity<GetHotelDetailResponseEvent> getHotelDetails(@PathVariable Long id) {
        return hotelService.getHotelDetails(id);
    }

    @GetMapping
    ResponseEntity<GetHotelsResponseEvent> getHotels(@RequestParam(required = false) String country,
                                                     @RequestParam(required = false) String city) {
        if (country == null || city == null) {
            return hotelService.getHotels();
        }
        return hotelService.getHotels(country, city);
    }

}
