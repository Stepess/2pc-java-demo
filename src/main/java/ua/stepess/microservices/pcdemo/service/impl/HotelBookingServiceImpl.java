package ua.stepess.microservices.pcdemo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ua.stepess.microservices.pcdemo.domain.HotelBooking;
import ua.stepess.microservices.pcdemo.persistence.HotelBookingRepository;
import ua.stepess.microservices.pcdemo.service.HotelBookingService;

@Slf4j
@Service
@RequiredArgsConstructor
public class HotelBookingServiceImpl implements HotelBookingService {

    private final HotelBookingRepository repository;

    @Override
    public HotelBooking book(HotelBooking booking) {
        return repository.save(booking);
    }

    @Override
    public HotelBooking find(Long bookingId) {
        return repository.findById(bookingId)
                .orElseThrow();
    }

}
