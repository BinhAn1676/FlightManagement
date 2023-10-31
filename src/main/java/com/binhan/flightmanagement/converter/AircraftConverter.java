package com.binhan.flightmanagement.converter;

import com.binhan.flightmanagement.dto.AircraftDto;
import com.binhan.flightmanagement.exception.AircraftNotFoundException;
import com.binhan.flightmanagement.models.AircraftEntity;
import com.binhan.flightmanagement.repository.AircraftRepository;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.repository.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AircraftConverter {
    private ModelMapper modelMapper;
    private AircraftRepository aircraftRepository;

    @Autowired
    public AircraftConverter(ModelMapper modelMapper,AircraftRepository aircraftRepository){
        this.aircraftRepository=aircraftRepository;
        this.modelMapper=modelMapper;
    }

    public AircraftDto convertToDto(AircraftEntity aircraftEntity){
        AircraftDto aircraftDto = modelMapper.map(aircraftEntity,AircraftDto.class);
        return aircraftDto;
    }

    public AircraftEntity convertToEntity(AircraftDto aircraftDto) {
        AircraftEntity aircraftEntity ;
        if(aircraftDto.getId()!=null){
            aircraftEntity=aircraftRepository.findById(aircraftDto.getId()).get();
            if(aircraftEntity==null){
                throw new AircraftNotFoundException("cant find aircraft");
            }
        }
        aircraftEntity=modelMapper.map(aircraftDto,AircraftEntity.class);
        return aircraftEntity;
    }
}
