package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.FlightConverter;
import com.binhan.flightmanagement.dto.FlightDto;
import com.binhan.flightmanagement.exception.FlightNotFoundException;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.repository.ReservationRepository;
import com.binhan.flightmanagement.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    private FlightConverter flightConverter;
    private ReservationRepository reservationRepository;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository,FlightConverter flightConverter,
                             ReservationRepository reservationRepository){
        this.flightRepository=flightRepository;
        this.flightConverter=flightConverter;
        this.reservationRepository=reservationRepository;
    }

    @Override
    public List<FlightEntity> findAll() {
        return flightRepository.findAll();
    }

    @Override
    public FlightEntity save(FlightDto newFlight) {
        FlightEntity flightEntity = flightConverter.convertToEntity(newFlight);
        return flightRepository.save(flightEntity);
    }

    @Override
    public List<FlightDto> findAllFlights() {
        List<FlightEntity> flightEntities = flightRepository.findAll();
        List<FlightDto> flightDtos = flightEntities
                .stream()
                .map(item ->flightConverter.convertDto(item))
                .collect(Collectors.toList());
        return flightDtos;
    }

    @Override
    public void saveFlightsByExcel(List<FlightEntity> flightEntities) {
        List<FlightEntity> flights = new ArrayList<>();
        for (FlightEntity item:flightEntities) {
            Boolean check = flightRepository.existsByAircraftAndDepartureAirportAndArrivalAirportAndStatusAndSeats(item.getAircraft(),
                    item.getDepartureAirport(),item.getArrivalAirport(),item.getStatus(),item.getSeats());
            if(check!=true){
                FlightEntity flight = FlightEntity
                        .builder()
                        .seats(item.getSeats())
                        .aircraft(item.getAircraft())
                        .arrivalTime(item.getArrivalTime())
                        .departureAirport(item.getDepartureAirport())
                        .departureTime(item.getDepartureTime())
                        .status(item.getStatus())
                        .arrivalAirport(item.getArrivalAirport())
                        .ticketPrice(item.getTicketPrice())
                        .build();
                flights.add(flight);
            }
        }
        flightRepository.saveAllAndFlush(flights);
    }

    @Override
    public void delete(Long id) {
        if(flightRepository.existsById(id)){
            reservationRepository.deleteByFlight_Id(id);
            flightRepository.deleteById(id);
        }else{
            throw new FlightNotFoundException("Cant find the flight");
        }
    }
}
