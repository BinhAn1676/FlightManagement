package com.binhan.flightmanagement.models;

import jakarta.persistence.*;
import lombok.*;


import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reservations")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    private FlightEntity flight;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "reservation_time")
    private Date reservationTime;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column(name = "booking_status")
    private String bookingStatus;
}
