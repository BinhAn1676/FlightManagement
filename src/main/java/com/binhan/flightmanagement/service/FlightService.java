package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.FlightDto;
import com.binhan.flightmanagement.models.FlightEntity;

import java.util.List;

public interface FlightService {
    List<FlightEntity> findAll();

    FlightEntity save(FlightDto newFlight);

    List<FlightDto> findAllFlights();

    void saveFlightsByExcel(List<FlightEntity> flightEntities);
}
