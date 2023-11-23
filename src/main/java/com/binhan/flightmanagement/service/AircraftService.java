package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.AircraftDto;
import com.binhan.flightmanagement.models.AircraftEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AircraftService {
    List<AircraftDto> findAll();

    AircraftEntity save(AircraftDto aircraftDto);

    void saveAircraftsByExcel(List<AircraftEntity> aircraftEntities);

    void delete(Long id);

    AircraftEntity findById(Long id);

    List<AircraftDto> findAircraftsWithSorting(String field);

    Page<AircraftDto> findAircraftsWithPaginationAndSorting(int offset, int pageSize, String field);
}
