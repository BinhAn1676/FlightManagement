package com.binhan.flightmanagement.dto;

import com.binhan.flightmanagement.models.CountryEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirportDto {
    private Long id;
    private String airportName;
    private String location;
    private Long countryId;
}
