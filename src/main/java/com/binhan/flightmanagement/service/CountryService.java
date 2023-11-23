package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.models.CountryEntity;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CountryService {
    List<CountryEntity> findAll();
    List<CountryDto> findAllCountries();

    CountryEntity save(CountryDto newCountry);

    void delete(Long id);

    void saveCountríeByExcel(List<CountryEntity> countryEntities);

    CountryEntity findById(Long id);

    List<CountryDto> findCountriesWithSorting(String field);

    Page<CountryDto> findCountriesWithPaginationAndSorting(int offset, int pageSize, String field);

    //void saveCountriesByExcel(MultipartFile file);
}
