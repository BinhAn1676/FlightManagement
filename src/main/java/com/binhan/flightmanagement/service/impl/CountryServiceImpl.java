package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.CountryConverter;
import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.exception.CountryExistedException;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.repository.CountryRepository;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.repository.ReservationRepository;
import com.binhan.flightmanagement.service.CountryService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class CountryServiceImpl implements CountryService {
    private CountryRepository countryRepository;
    private AirportRepository airportRepository;
    private FlightRepository flightRepository;
    private CountryConverter countryConverter;
    private ReservationRepository reservationRepository;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository,AirportRepository airportRepository,
                              FlightRepository flightRepository, CountryConverter countryConverter,
                              ReservationRepository reservationRepository){
        this.countryRepository=countryRepository;
        this.reservationRepository=reservationRepository;
        this.flightRepository=flightRepository;
        this.airportRepository=airportRepository;
        this.countryConverter=countryConverter;
    }

    @Override
    public List<CountryEntity> findAll() {
        return countryRepository.findAll();
    }

    @Override
    public List<CountryDto> findAllCountries() {
        List<CountryEntity> countryEntities= countryRepository.findAll();
        List<CountryDto> countryDtos = countryEntities
                .stream()
                .map(item -> countryConverter.convertToDto(item))
                .collect(Collectors.toList());
        return countryDtos;
    }

    @Override
    public CountryEntity save(CountryDto country) {
        CountryEntity countryEntity;
        if(countryRepository.existsByCountryName(country.getCountryName())){
            throw new CountryExistedException("Country is already in database");
        }
        if(country.getId()!=null){
            countryEntity=countryRepository.findById(country.getId()).get();
        }else{
            countryEntity = new CountryEntity();
        }
        countryEntity.setCountryName(country.getCountryName());
        return countryRepository.save(countryEntity);
    }

    @Override
    public void delete(Long id) {
        CountryEntity countryEntity = countryRepository.findById(id).get();
        List<Long> airportsId = countryEntity.getAirports()
                .stream().map(item -> item.getId())
                .collect(Collectors.toList());
        List<FlightEntity> flights1 = flightRepository.findByArrivalAirport_IdIn(airportsId);
        List<FlightEntity> flights2 = flightRepository.findByDepartureAirport_IdIn(airportsId);
        List<FlightEntity> flights =  Stream.concat(flights1.stream(), flights2.stream())
                .distinct()
                .collect(Collectors.toList());
        List<Long> ids = flights.stream().map(item->item.getId()).collect(Collectors.toList());
        for (Long item:ids){
            reservationRepository.deleteByFlight_Id(item);
        }
        flightRepository.deleteByIdIn(ids);
        airportRepository.deleteByIdIn(airportsId);
        countryRepository.deleteById(id);
    }

    @Override
    public void saveCountríeByExcel(List<CountryEntity> countryEntities) {
        for (CountryEntity item:countryEntities) {
            Boolean check = countryRepository.existsByCountryName(item.getCountryName());
            if(check==false){
                CountryEntity country = CountryEntity
                        .builder()
                        .countryName(item.getCountryName())
                        .build();
                countryRepository.saveAndFlush(country);
            }
        }
    }

    /*@Override
    public void saveCountriesByExcel(MultipartFile file) {
        if(excelUpload.isValidExcelFile(file)){
            try {
                List<CountryEntity> countryEntities=excelUpload.getCountriesDataFromExcel(file.getInputStream());
                for(var country:countryEntities){
                    if(countryRepository.findByCountryName(country.getCountryName())==null){
                        countryRepository.save(country);
                    }
                }
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }*/

}
