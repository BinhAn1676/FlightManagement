package com.binhan.flightmanagement.repository.custom;

import com.binhan.flightmanagement.dto.FilterFlightDto;
import com.binhan.flightmanagement.models.FlightEntity;

import java.util.List;

public interface FlightRepositoryCustom {
    List<FlightEntity> filterFlights(FilterFlightDto filterFlightDto);
}
