package ua.stepess.microservices.pcdemo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.stepess.microservices.pcdemo.domain.TripBooking;
import ua.stepess.microservices.pcdemo.service.FlyBookingService;
import ua.stepess.microservices.pcdemo.service.HotelBookingService;
import ua.stepess.microservices.pcdemo.service.TripBookingService;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripBookingServiceImpl implements TripBookingService {

    private final FlyBookingService flyBookingService;
    private final HotelBookingService hotelBookingService;

    @Override
    public TripBooking book(TripBooking booking) {
        return null;
    }

    @Override
    public TripBooking find(Long bookingId) {
        return null;
    }

}
