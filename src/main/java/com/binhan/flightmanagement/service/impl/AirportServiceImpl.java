package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.AirportConverter;
import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AirportServiceImpl implements AirportService {
    private AirportRepository airportRepository;
    private AirportConverter airportConverter;

    @Autowired
    public AirportServiceImpl(AirportRepository airportRepository,AirportConverter airportConverter){
        this.airportRepository=airportRepository;
        this.airportConverter=airportConverter;
    }

    @Override
    public List<AirportEntity> findAll() {
        return airportRepository.findAll();
    }

    @Override
    public AirportEntity save(AirportDto airport) {
        AirportEntity airportEntity= airportConverter.convertToEntity(airport);
        return airportRepository.save(airportEntity);
    }
}
