package ua.stepess.microservices.pcdemo.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.stepess.microservices.pcdemo.domain.TripBooking;
import ua.stepess.microservices.pcdemo.service.TripBookingService;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor
public class BookingController {

    private final TripBookingService tripBookingService;

    @GetMapping("/{id}")
    public TripBooking getBook(@PathVariable Long id) {
        return tripBookingService.find(id);
    }

}
