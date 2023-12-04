package com.binhan.flightmanagement.converter;

import com.binhan.flightmanagement.dto.ReservationDto;
import com.binhan.flightmanagement.dto.request.ReservationRequestDto;
import com.binhan.flightmanagement.models.ReservationEntity;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ReservationConverter {
    private FlightRepository flightRepository;
    private UserRepository userRepository;

    @Autowired
    public ReservationConverter(FlightRepository flightRepository,UserRepository userRepository){
        this.flightRepository=flightRepository;
        this.userRepository=userRepository;
    }

    public ReservationDto convertToDto(ReservationEntity  reservationEntity){
        ReservationDto reservationDto = ReservationDto
                .builder()
                .bookingStatus(reservationEntity.getBookingStatus())
                .seatNumber(reservationEntity.getSeatNumber())
                .id(reservationEntity.getId())
                .username(reservationEntity.getUser().getUsername())
                .flightId(reservationEntity.getFlight().getId())
                .reservationTime(reservationEntity.getReservationTime())
                .build();
        return reservationDto;
    }

    public ReservationEntity convertToEntity(ReservationRequestDto reservationDto, Integer seatNumber) {
        ReservationEntity reservationEntity;
        if(reservationDto.getId()!=null){
            reservationEntity = ReservationEntity
                    .builder()
                    .id(reservationDto.getId())
                    .user(userRepository.findByUserName(reservationDto.getUsername()).get())
                    .seatNumber(seatNumber)
                    .flight(flightRepository.findById(reservationDto.getFlightId()).get())
                    .bookingStatus("Not paid")
                    .reservationTime(new Date())
                    .build();
        }else{
            reservationEntity = ReservationEntity
                    .builder()
                    .user(userRepository.findByUserName(reservationDto.getUsername()).get())
                    .seatNumber(seatNumber)
                    .flight(flightRepository.findById(reservationDto.getFlightId()).get())
                    .bookingStatus("Not paid")
                    .reservationTime(new Date())
                    .build();
        }
        return reservationEntity;
    }
}
