package ua.stepess.microservices.pcdemo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.stepess.microservices.pcdemo.domain.FlyBooking;
import ua.stepess.microservices.pcdemo.persistence.FlyBookingRepository;
import ua.stepess.microservices.pcdemo.service.FlyBookingService;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlyBookingServiceImpl implements FlyBookingService {

    private final FlyBookingRepository repository;

    @Override
    public FlyBooking book(FlyBooking booking) {
        return repository.save(booking);
    }

    @Override
    public FlyBooking find(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow();
    }

}
