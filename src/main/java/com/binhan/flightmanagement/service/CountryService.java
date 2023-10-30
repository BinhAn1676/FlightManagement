package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.models.CountryEntity;

import java.util.List;

public interface CountryService {
    List<CountryEntity> findAll();

    CountryEntity save(CountryDto newCountry);
}
