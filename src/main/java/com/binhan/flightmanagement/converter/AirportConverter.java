package com.binhan.flightmanagement.converter;

import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.exception.CountryNotFoundException;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.repository.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AirportConverter {
    private ModelMapper modelMapper;
    private CountryRepository countryRepository;

    private AirportRepository airportRepository;

    @Autowired
    public AirportConverter(ModelMapper modelMapper,CountryRepository countryRepository,AirportRepository airportRepository){
        this.modelMapper=modelMapper;
        this.countryRepository=countryRepository;
        this.airportRepository=airportRepository;
    }

    public AirportEntity convertToEntity(AirportDto airportDto){
        AirportEntity airportEntity;
        if(airportDto.getId()!=null){
            airportEntity = airportRepository.findById(airportDto.getId()).orElse(new AirportEntity());
        }
        airportEntity = modelMapper.map(airportDto,AirportEntity.class);
        //CountryEntity country = countryRepository.findById(airportDto.getCountryId()).get();
        CountryEntity country = countryRepository.findByCountryName(airportDto.getCountryName());
        if(country==null){
            throw new CountryNotFoundException("Cant find the country " + airportDto.getAirportName());
        }
        airportEntity.setCountry(country);
        return airportEntity;
    }
    public AirportDto convertToDto(AirportEntity airportEntity){
        AirportDto airportDto = modelMapper.map(airportEntity,AirportDto.class);
        airportDto.setCountryName(airportEntity.getCountry().getCountryName());
        return airportDto;
    }
}
