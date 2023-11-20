package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.models.CountryEntity;

import java.util.List;

public interface CountryService {
    List<CountryEntity> findAll();
    List<CountryDto> findAllCountries();

    CountryEntity save(CountryDto newCountry);

    void delete(Long id);

    void saveCountríeByExcel(List<CountryEntity> countryEntities);

    CountryEntity findById(Long id);

    //void saveCountriesByExcel(MultipartFile file);
}
