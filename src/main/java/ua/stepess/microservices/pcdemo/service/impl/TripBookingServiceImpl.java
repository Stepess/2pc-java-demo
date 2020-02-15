package ua.stepess.microservices.pcdemo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.stepess.microservices.pcdemo.TimeUtils;
import ua.stepess.microservices.pcdemo.domain.TripBooking;
import ua.stepess.microservices.pcdemo.service.FlyBookingService;
import ua.stepess.microservices.pcdemo.service.HotelBookingService;
import ua.stepess.microservices.pcdemo.service.TripBookingService;

import javax.transaction.Transactional;
import java.util.Objects;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class TripBookingServiceImpl implements TripBookingService {

    private final FlyBookingService flyBookingService;
    private final HotelBookingService hotelBookingService;

    @Override
    @Transactional
    public TripBooking book(TripBooking booking) {
        log.info("Booking trip: client [{}]", booking.getFlyBooking().getClientName());

        var flyBooking = flyBookingService.book(booking.getFlyBooking());
        log.debug("Fly booking precommit successful");

        Long timeToSleep = booking.getWait();

        if (! isNull(timeToSleep) && timeToSleep > 0) {
            TimeUtils.sleep(timeToSleep);
        }

        if (booking.getFail()) {
            throw new RuntimeException();
        }

        var hotelBooking = hotelBookingService.book(booking.getHotelBooking());
        log.debug("Hotel booking precommit successful");

        return new TripBooking(flyBooking, hotelBooking, null, null);
    }

    @Override
    public TripBooking find(Long bookingId) {
        return null;
    }

}
