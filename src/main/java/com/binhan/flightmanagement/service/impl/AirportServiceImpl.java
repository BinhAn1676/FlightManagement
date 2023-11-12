package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.AirportConverter;
import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.repository.ReservationRepository;
import com.binhan.flightmanagement.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class AirportServiceImpl implements AirportService {
    private AirportRepository airportRepository;
    private AirportConverter airportConverter;
    private FlightRepository flightRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public AirportServiceImpl(AirportRepository airportRepository,AirportConverter airportConverter,
                              FlightRepository flightRepository,ReservationRepository reservationRepository){
        this.airportRepository=airportRepository;
        this.reservationRepository=reservationRepository;
        this.flightRepository=flightRepository;
        this.airportConverter=airportConverter;
    }

    @Override
    public List<AirportEntity> findAll() {
        return airportRepository.findAll();
    }

    @Override
    public List<AirportDto> findAllAirports() {
        List<AirportEntity> airportEntities = airportRepository.findAll();
        List<AirportDto> airportDtos = airportEntities
                .stream()
                .map(item -> airportConverter.convertToDto(item))
                .collect(Collectors.toList());
        return airportDtos;
    }

    @Override
    public AirportEntity save(AirportDto airport) {
        AirportEntity airportEntity= airportConverter.convertToEntity(airport);
        return airportRepository.save(airportEntity);
    }

    @Override
    public void saveAirportsByExcel(List<AirportEntity> airportEntities) {
        List<AirportEntity> airports = new ArrayList<>();
        for (AirportEntity item:airports) {
            AirportEntity airport = airportRepository.findByAirportName(item.getAirportName());
            if(airport==null) {
                airport= AirportEntity.builder()
                        .airportName(item.getAirportName())
                        .location(item.getLocation())
                        .country(item.getCountry())
                        .build();
            }
            airports.add(airport);
        }
        airportRepository.saveAllAndFlush(airports);
    }

    @Override
    public void delete(Long id) {
        List<FlightEntity> flights1 = flightRepository.findByArrivalAirport_Id(id);
        List<FlightEntity> flights2 = flightRepository.findByDepartureAirport_Id(id);
        List<FlightEntity> flights =  Stream.concat(flights1.stream(), flights2.stream())
                .distinct()
                .collect(Collectors.toList());
        List<Long> ids = flights.stream().map(item->item.getId()).collect(Collectors.toList());
        for (Long item:ids){
            reservationRepository.deleteByFlight_Id(item);
        }
        flightRepository.deleteByIdIn(ids);
        airportRepository.deleteById(id);
    }
}
