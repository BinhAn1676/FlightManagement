package com.binhan.flightmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AircraftDto {
    private Long id;

    private String aircraftName;

    private Integer seats;

    private String type;
}
