package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.repository.CountryRepository;
import com.binhan.flightmanagement.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    private CountryRepository countryRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository){
        this.countryRepository=countryRepository;
    }

    @Override
    public List<CountryEntity> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public CountryEntity save(CountryDto country) {
        CountryEntity countryEntity;
        if(country.getId()!=null){
            countryEntity=countryRepository.findById(country.getId()).get();
        }else{
            countryEntity = new CountryEntity();
        }
        countryEntity.setCountryName(country.getName());
        return countryRepository.save(countryEntity);
    }
}
