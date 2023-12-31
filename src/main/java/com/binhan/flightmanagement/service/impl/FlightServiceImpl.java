package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.FlightConverter;
import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.dto.FilterFlightDto;
import com.binhan.flightmanagement.dto.FlightDto;
import com.binhan.flightmanagement.exception.FlightNotFoundException;
import com.binhan.flightmanagement.exception.WrongDateFormatException;
import com.binhan.flightmanagement.exception.WrongDateLogicException;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.repository.ReservationRepository;
import com.binhan.flightmanagement.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
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
                .map(item ->flightConverter.convertToDto(item))
                .collect(Collectors.toList());
        return flightDtos;
    }

    @Override
    public void saveFlightsByExcel(List<FlightEntity> flightEntities) {
        for (FlightEntity item:flightEntities) {
            Boolean check = flightRepository.existsByAircraftAndDepartureAirportAndArrivalAirportAndStatusAndSeatsAndArrivalTimeAndDepartureTime(item.getAircraft(),
                    item.getDepartureAirport(),item.getArrivalAirport(),item.getStatus(),item.getSeats(),item.getArrivalTime(),item.getDepartureTime());
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
                flightRepository.saveAndFlush(flight);
            }
        }

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

    @Override
    public FlightEntity findById(Long id) {
        FlightEntity flightEntity = flightRepository.findById(id).get();
        if(flightEntity==null){
            throw new FlightNotFoundException("cant find flight");
        }
        return flightEntity;
    }

    @Override
    public List<FlightDto> findCountriesWithSorting(String field) {
        List<FlightEntity> flightEntities;
        if(field == null){
            flightEntities = flightRepository.findAll();
        }else{
            flightEntities = flightRepository.findAll(Sort.by(Sort.Direction.ASC,field));
        }
        List<FlightDto> flightDtos = flightEntities.stream()
                .map(item->flightConverter.convertToDto(item))
                .collect(Collectors.toList());
        return flightDtos;
    }

    @Override
    public Page<FlightDto> findCountriesWithPaginationAndSorting(int offset, int pageSize, String field) {
        Page<FlightEntity> flightEntities;
        if (field == null) {
            flightEntities = flightRepository.findAll(PageRequest.of(offset, pageSize));
        } else {
            flightEntities = flightRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        }
        return flightEntities.map(item->flightConverter.convertToDto(item));
    }

    @Override
    public List<FlightDto> findFlightFilter(FilterFlightDto filterFlightDto) {
        if(filterFlightDto.getDepartureTime()!=null){
            filterFlightDto.setDepartureTime(filterFlightDto.getDepartureTime().replace("/","-"));
        }else if(filterFlightDto.getArrivalTime()!=null){
            filterFlightDto.setArrivalTime(filterFlightDto.getArrivalTime().replace("/","-"));
        }
        List<FlightEntity> flightEntities = flightRepository.filterFlights(filterFlightDto);
        List<FlightDto> flightDtos = flightEntities.stream()
                .map(flightConverter::convertToDto)
                .collect(Collectors.toList());
        return flightDtos;
    }
    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            //formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format", e);
        }
    }
}
