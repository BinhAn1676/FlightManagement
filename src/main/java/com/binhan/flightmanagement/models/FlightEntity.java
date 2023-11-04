package com.binhan.flightmanagement.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class FlightEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "departure_time")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date departureTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrival_time")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private Date arrivalTime;

    private String status;
    private Integer seats;
    private Double ticketPrice;
    @ManyToOne
    @JoinColumn(name = "departure_airport_id")
    private AirportEntity departureAirport;

    @ManyToOne
    @JoinColumn(name = "arrival_airport_id")
    private AirportEntity arrivalAirport;

    @ManyToOne
    @JoinColumn(name = "aircraft_id")
    private AircraftEntity aircraft;

}
