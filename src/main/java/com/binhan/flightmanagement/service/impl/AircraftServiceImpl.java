package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.AircraftConverter;
import com.binhan.flightmanagement.dto.AircraftDto;
import com.binhan.flightmanagement.models.AircraftEntity;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.AircraftRepository;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.repository.ReservationRepository;
import com.binhan.flightmanagement.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AircraftServiceImpl implements AircraftService {
    private AircraftRepository aircraftRepository;
    private AircraftConverter aircraftConverter;
    private FlightRepository flightRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public AircraftServiceImpl(AircraftRepository aircraftRepository, AircraftConverter aircraftConverter,
                               FlightRepository flightRepository,ReservationRepository reservationRepository) {
        this.flightRepository=flightRepository;
        this.aircraftRepository = aircraftRepository;
        this.aircraftConverter = aircraftConverter;
        this.reservationRepository=reservationRepository;
    }


    @Override
    public List<AircraftDto> findAll() {
        List<AircraftEntity> aircraftEntities = aircraftRepository.findAll();
        List<AircraftDto> aircraftDtos = aircraftEntities.stream()
                .map(item -> aircraftConverter.convertToDto(item))
                .collect(Collectors.toList());
        return aircraftDtos;
    }

    @Override
    public AircraftEntity save(AircraftDto aircraftDto) {
        AircraftEntity aircraftEntity = aircraftConverter.convertToEntity(aircraftDto);
        return aircraftRepository.save(aircraftEntity);
    }

    @Override
    public void saveAircraftsByExcel(List<AircraftEntity> aircraftEntities) {
        List<AircraftEntity> aircrafts = new ArrayList<>();
        for (AircraftEntity item : aircraftEntities) {
            Boolean check = aircraftRepository.existsByAircraftName(item.getAircraftName());
            if (check != true) {
                AircraftEntity aircraft = AircraftEntity
                        .builder()
                        .aircraftName(item.getAircraftName())
                        .type(item.getType())
                        .seats(item.getSeats())
                        .build();
                aircrafts.add(aircraft);
            }
            aircraftRepository.saveAllAndFlush(aircrafts);
        }
    }

    @Override
    public void delete(Long id) {
        List<FlightEntity> flightEntities = flightRepository.findByAircraft_Id(id);
        List<Long> ids = flightEntities
                .stream()
                .map(item -> item.getId())
                .collect(Collectors.toList());;
        flightRepository.deleteByIdIn(ids);
        reservationRepository.deleteByFlight_Id(id);
        aircraftRepository.deleteById(id);
    }
}
