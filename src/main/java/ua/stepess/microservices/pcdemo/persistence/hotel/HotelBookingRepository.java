package ua.stepess.microservices.pcdemo.persistence.hotel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.stepess.microservices.pcdemo.domain.hotel.HotelBooking;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {
}
