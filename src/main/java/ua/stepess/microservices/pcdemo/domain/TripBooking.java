package ua.stepess.microservices.pcdemo.domain;

import lombok.Data;

@Data
public class TripBooking {
    private FlyBooking flyBooking;
    private HotelBooking hotelBooking;
}
