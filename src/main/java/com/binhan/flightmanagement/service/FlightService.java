package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.FilterFlightDto;
import com.binhan.flightmanagement.dto.FlightDto;
import com.binhan.flightmanagement.models.FlightEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FlightService {
    List<FlightEntity> findAll();

    FlightEntity save(FlightDto newFlight);

    List<FlightDto> findAllFlights();

    void saveFlightsByExcel(List<FlightEntity> flightEntities);

    void delete(Long id);

    FlightEntity findById(Long id);

    List<FlightDto> findCountriesWithSorting(String field);

    Page<FlightDto> findCountriesWithPaginationAndSorting(int offset, int pageSize, String field);

    List<FlightDto> findFlightFilter(FilterFlightDto filterFlightDto);
}
