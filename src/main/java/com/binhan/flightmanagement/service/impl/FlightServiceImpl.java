package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.FlightConverter;
import com.binhan.flightmanagement.dto.FlightDto;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    private FlightConverter flightConverter;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository,FlightConverter flightConverter){
        this.flightRepository=flightRepository;
        this.flightConverter=flightConverter;
    }

    @Override
    public List<FlightEntity> findAll() {
        return flightRepository.findAll();
    }

    @Override
    public FlightEntity save(FlightDto newFlight) {
        FlightEntity flightEntity = flightConverter.convertToEntity(newFlight);
        return flightRepository.save(flightEntity);
    }

    @Override
    public List<FlightDto> findAllFlights() {
        return null;
    }
}
