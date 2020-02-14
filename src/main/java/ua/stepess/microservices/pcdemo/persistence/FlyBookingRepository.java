package ua.stepess.microservices.pcdemo.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.stepess.microservices.pcdemo.domain.FlyBooking;

@Repository
public interface FlyBookingRepository extends JpaRepository<FlyBooking, Long> {
}
