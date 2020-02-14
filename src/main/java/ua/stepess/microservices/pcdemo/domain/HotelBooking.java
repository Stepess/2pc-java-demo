package ua.stepess.microservices.pcdemo.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "hotel_booking")
public class HotelBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "hotel_name")
    private String hotelName;
    @Column(name = "arrival_date")
    private LocalDate arrival;
    @Column(name = "depurture_date")
    private LocalDate departure;
}
