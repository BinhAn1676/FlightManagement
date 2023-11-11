package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.AirportConverter;
import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
    public List<AirportDto> findAllAirports() {
        List<AirportEntity> airportEntities = airportRepository.findAll();
        List<AirportDto> airportDtos = airportEntities
                .stream()
                .map(item -> airportConverter.convertToDto(item))
                .collect(Collectors.toList());
        return airportDtos;
    }

    @Override
    public AirportEntity save(AirportDto airport) {
        AirportEntity airportEntity= airportConverter.convertToEntity(airport);
        return airportRepository.save(airportEntity);
    }

    @Override
    public void saveAirportsByExcel(List<AirportEntity> airportEntities) {
        List<AirportEntity> airports = new ArrayList<>();
        for (AirportEntity item:airports) {
            AirportEntity airport = airportRepository.findByAirportName(item.getAirportName());
            if(airport==null) {
                airport= AirportEntity.builder()
                        .airportName(item.getAirportName())
                        .location(item.getLocation())
                        .country(item.getCountry())
                        .build();
            }
            airports.add(airport);
        }
        airportRepository.saveAllAndFlush(airports);
    }
}
