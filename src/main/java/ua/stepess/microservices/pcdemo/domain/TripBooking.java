package ua.stepess.microservices.pcdemo.domain;

import lombok.Data;
import ua.stepess.microservices.pcdemo.domain.fly.FlyBooking;
import ua.stepess.microservices.pcdemo.domain.hotel.HotelBooking;

@Data
public class TripBooking {
    private FlyBooking flyBooking;
    private HotelBooking hotelBooking;
}
