package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.AircraftConverter;
import com.binhan.flightmanagement.dto.AircraftDto;
import com.binhan.flightmanagement.models.AircraftEntity;
import com.binhan.flightmanagement.repository.AircraftRepository;
import com.binhan.flightmanagement.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AircraftServiceImpl implements AircraftService {
    private AircraftRepository aircraftRepository;
    private AircraftConverter aircraftConverter;

    @Autowired
    public AircraftServiceImpl(AircraftRepository aircraftRepository,AircraftConverter aircraftConverter){
        this.aircraftRepository=aircraftRepository;
        this.aircraftConverter=aircraftConverter;
    }


    @Override
    public List<AircraftDto> findAll() {
        List<AircraftEntity> aircraftEntities = aircraftRepository.findAll();
        List<AircraftDto> aircraftDtos = aircraftEntities.stream()
                .map(item->aircraftConverter.convertToDto(item))
                .collect(Collectors.toList());
        return aircraftDtos;
    }

    @Override
    public AircraftEntity save(AircraftDto aircraftDto) {
        AircraftEntity aircraftEntity =aircraftConverter.convertToEntity(aircraftDto);
        return aircraftRepository.save(aircraftEntity);
    }
}
