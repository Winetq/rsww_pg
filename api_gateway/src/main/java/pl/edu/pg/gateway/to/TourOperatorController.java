package pl.edu.pg.gateway.to;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.edu.pg.gateway.to.dto.TOUpdateResponse;

import java.util.List;

@RestController
@RequestMapping("api")
class TourOperatorController {
    private final TourOperatorService tourOperatorService;

    @Autowired
    TourOperatorController(TourOperatorService tourOperatorService) {
        this.tourOperatorService = tourOperatorService;
    }

    @GetMapping("to-latest-updates")
    ResponseEntity<List<TOUpdateResponse>> getTOLatestUpdates() {
        return tourOperatorService.getTOLatestUpdates();
    }
}
