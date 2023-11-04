package com.binhan.flightmanagement.converter;

import com.binhan.flightmanagement.controllers.FlightController;
import com.binhan.flightmanagement.dto.FlightDto;
import com.binhan.flightmanagement.exception.FlightNotFoundException;
import com.binhan.flightmanagement.exception.WrongDateFormatException;
import com.binhan.flightmanagement.exception.WrongDateLogicException;
import com.binhan.flightmanagement.models.AircraftEntity;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.AircraftRepository;
import com.binhan.flightmanagement.repository.AirportRepository;
import com.binhan.flightmanagement.repository.FlightRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


@Component
public class FlightConverter {

    private FlightRepository flightRepository;
    private ModelMapper modelMapper;
    private AircraftRepository aircraftRepository;
    private AirportRepository airportRepository;

    @Autowired
    public FlightConverter(FlightRepository flightRepository,ModelMapper modelMapper,
                            AirportRepository airportRepository,AircraftRepository aircraftRepository){
        this.flightRepository=flightRepository;
        this.modelMapper=modelMapper;
        this.airportRepository=airportRepository;
        this.aircraftRepository=aircraftRepository;
    }

    public FlightEntity convertToEntity(FlightDto newFlight) {
        FlightEntity flightEntity ;
        if(newFlight.getId()!=null){
            flightEntity=flightRepository.findById(newFlight.getId()).get();
            if(flightEntity==null){
                throw new FlightNotFoundException("Cant find the flight");
            }
        }else{
            flightEntity = new FlightEntity();
        }
        flightEntity.setSeats(newFlight.getSeats());
        flightEntity.setStatus(newFlight.getStatus());
        flightEntity.setTicketPrice(newFlight.getTicketPrice());
        if(newFlight.getAircraftId()!=null){
            AircraftEntity aircraftEntity= aircraftRepository.findById(newFlight.getAircraftId()).get();
            flightEntity.setAircraft(aircraftEntity);
        }
        if(newFlight.getDepartureAirportId()!=null){
            AirportEntity departureAirport = airportRepository.findById(newFlight.getDepartureAirportId()).get();
            flightEntity.setDepartureAirport(departureAirport);
        }
        if(newFlight.getArrivalAirportId()!=null){
            AirportEntity arrivalAirport = airportRepository.findById(newFlight.getArrivalAirportId()).get();
            flightEntity.setArrivalAirport(arrivalAirport);
        }
        flightEntity = parseDate(flightEntity,newFlight);
        return flightEntity;
    }

    private FlightEntity parseDate(FlightEntity flightEntity, FlightDto newFlight) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm dd/MM/yyyy");
        try {
            flightEntity.setArrivalTime(dateFormat.parse(newFlight.getArrivalTime()));
            flightEntity.setDepartureTime(dateFormat.parse(newFlight.getDepartureTime()));
            if(flightEntity.getDepartureTime().compareTo(flightEntity.getArrivalTime())>=0){
                throw new WrongDateLogicException("Illegal date");
            }
        } catch (ParseException e) {
            throw new WrongDateFormatException("wrong date format");
        }
        return flightEntity;
    }
}
