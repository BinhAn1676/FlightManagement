package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.AircraftDto;
import com.binhan.flightmanagement.models.AircraftEntity;

import java.util.List;

public interface AircraftService {
    List<AircraftDto> findAll();

    AircraftEntity save(AircraftDto aircraftDto);

    void saveAircraftsByExcel(List<AircraftEntity> aircraftEntities);
}
