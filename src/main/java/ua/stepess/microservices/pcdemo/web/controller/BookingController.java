package ua.stepess.microservices.pcdemo.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping
    public TripBooking book(@RequestBody TripBooking tripBooking) {
        return tripBookingService.book(tripBooking);
    }

}
