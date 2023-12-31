package com.binhan.flightmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDto {
    private Long id;
    private String departureTime;
    private String arrivalTime;

    private String status;
    private Integer seats;

    private Double ticketPrice;

    private String departureAirport;

    private String arrivalAirport;

    private Long aircraftId;
}
