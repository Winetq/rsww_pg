package pl.edu.pg.gateway.transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pg.gateway.transport.dto.GetFlightDetailsResponse;

import java.util.List;

@RestController
@RequestMapping("api/transports")
class TransportController {
    private final TransportService transportService;

    @Autowired
    TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("{id}")
    ResponseEntity<GetFlightDetailsResponse> getFlightDetails(@PathVariable Long id) {
        return transportService.getFlightDetails(id);
    }

    @GetMapping
    ResponseEntity<List<GetFlightDetailsResponse>> getFlights(@RequestParam(required = false) String departureAirport,
                                                              @RequestParam(required = false) String arrivalAirport,
                                                              @RequestParam(required = false) String departureDate,
                                                              @RequestParam(required = false) String arrivalDate) {
        if (departureAirport == null || arrivalAirport == null || departureDate == null || arrivalDate == null) {
            return transportService.getFlights();
        }
        return transportService.getFlights(departureAirport, arrivalAirport, departureDate, arrivalDate);
    }

}
