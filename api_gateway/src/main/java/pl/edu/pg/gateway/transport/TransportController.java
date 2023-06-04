package pl.edu.pg.gateway.transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.edu.pg.gateway.transport.dto.GetFlightDetailsResponse;

import java.util.List;

@RestController
@RequestMapping("api")
class TransportController {
    private final TransportService transportService;

    @Autowired
    TransportController(TransportService transportService) {
        this.transportService = transportService;
    }

    @GetMapping("transports/{id}")
    ResponseEntity<GetFlightDetailsResponse> getFlightDetails(@PathVariable Long id) {
        return transportService.getFlightDetails(id);
    }

    @GetMapping("transport")
    ResponseEntity<GetFlightDetailsResponse> getFlightWithParameters(@RequestParam String departureAirport,
                                                                     @RequestParam String arrivalAirport,
                                                                     @RequestParam String departureDate,
                                                                     @RequestParam String arrivalDate) {
        return transportService.getFlightWithParameters(departureAirport, arrivalAirport, departureDate, arrivalDate);
    }

    @GetMapping("transports")
    ResponseEntity<List<GetFlightDetailsResponse>> getFlights() {
        return transportService.getFlights();
    }

}
