package ua.stepess.microservices.pcdemo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ua.stepess.microservices.pcdemo.domain.fly.FlyBooking;
import ua.stepess.microservices.pcdemo.domain.hotel.HotelBooking;

@Data
@AllArgsConstructor
public class TripBooking {
    @JsonProperty("fly")
    private FlyBooking flyBooking;
    @JsonProperty("hotel")
    private HotelBooking hotelBooking;
    private Boolean fail;
    private Long wait;
}
