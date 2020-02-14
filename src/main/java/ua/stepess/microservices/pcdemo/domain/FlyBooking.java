package ua.stepess.microservices.pcdemo.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "fly_booking")
public class FlyBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "client_name")
    private String clientName;
    @Column(name = "fly_name")
    private String flyNumber;
    @Column(name = "arrival_place")
    private String from;
    @Column(name = "departure_place")
    private String to;
    @Column(name = "arrival_date")
    private LocalDate date;
}
