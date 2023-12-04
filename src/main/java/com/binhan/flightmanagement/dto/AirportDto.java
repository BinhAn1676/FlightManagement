package com.binhan.flightmanagement.dto;

import com.binhan.flightmanagement.models.CountryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirportDto {
    private Long id;
    private String airportName;
    private String location;
    private String countryName;
}
