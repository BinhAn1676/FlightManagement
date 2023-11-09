package com.binhan.flightmanagement.converter;

import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.models.CountryEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CountryConverter {

    private ModelMapper modelMapper;

    @Autowired
    public CountryConverter(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    public CountryDto convertToDto(CountryEntity countryEntity){
        CountryDto countryDto = modelMapper.map(countryEntity,CountryDto.class);
        return countryDto;
    }

}
