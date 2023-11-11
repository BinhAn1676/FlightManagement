package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.models.CountryEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CountryService {
    List<CountryEntity> findAll();
    List<CountryDto> findAllCountries();

    CountryEntity save(CountryDto newCountry);

    void delete(Long id);

    void saveCountríeByExcel(List<CountryEntity> countryEntities);

    //void saveCountriesByExcel(MultipartFile file);
}
