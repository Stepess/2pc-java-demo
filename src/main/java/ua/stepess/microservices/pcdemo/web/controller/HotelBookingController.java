package ua.stepess.microservices.pcdemo.web.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.stepess.microservices.pcdemo.domain.HotelBooking;
import ua.stepess.microservices.pcdemo.service.HotelBookingService;

@RestController
@RequestMapping("/booking/hotel")
@RequiredArgsConstructor
public class HotelBookingController {

    private final HotelBookingService hotelBookingService;

    @GetMapping("/{id}")
    public HotelBooking getBook(@PathVariable Long id) {
        return hotelBookingService.find(id);
    }

}
