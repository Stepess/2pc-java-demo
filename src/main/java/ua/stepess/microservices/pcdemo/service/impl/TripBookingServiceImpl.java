package ua.stepess.microservices.pcdemo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.stepess.microservices.pcdemo.domain.TripBooking;
import ua.stepess.microservices.pcdemo.service.FlyBookingService;
import ua.stepess.microservices.pcdemo.service.HotelBookingService;
import ua.stepess.microservices.pcdemo.service.TripBookingService;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripBookingServiceImpl implements TripBookingService {

    private final FlyBookingService flyBookingService;
    private final HotelBookingService hotelBookingService;

    @Override
    @Transactional
    public TripBooking book(TripBooking booking) {
        log.info("Saving booking trip booking");
        var flyBooking = flyBookingService.book(booking.getFlyBooking());
        var hotelBooking = hotelBookingService.book(booking.getHotelBooking());
        return new TripBooking(flyBooking, hotelBooking);
    }

    @Override
    public TripBooking find(Long bookingId) {
        return null;
    }

}
