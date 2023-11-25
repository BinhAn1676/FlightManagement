package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.ReservationConverter;
import com.binhan.flightmanagement.dto.ReservationDto;
import com.binhan.flightmanagement.dto.request.ReservationRequestDto;
import com.binhan.flightmanagement.exception.FlightNotFoundException;
import com.binhan.flightmanagement.exception.ReservationNotFoundException;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.models.ReservationEntity;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.repository.ReservationRepository;
import com.binhan.flightmanagement.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private ReservationConverter reservationConverter;
    private FlightRepository flightRepository;

    @Autowired
    public ReservationServiceImpl(ReservationRepository reservationRepository,ReservationConverter reservationConverter,
                                  FlightRepository flightRepository){
        this.reservationRepository=reservationRepository;
        this.reservationConverter=reservationConverter;
        this.flightRepository=flightRepository;
    }

    @Override
    public List<ReservationRequestDto> findAll() {
        List<ReservationEntity> reservationEntities = reservationRepository.findAll();
        Map<Long, List<ReservationEntity>> groupedByUserId = reservationEntities.stream()
                .collect(Collectors.groupingBy(reservationEntity -> reservationEntity.getUser().getId()));
        List<ReservationRequestDto> reservationDtos = groupedByUserId.entrySet().stream()
                .map(entry -> {
                    Long userId = entry.getKey();
                    List<ReservationEntity> reservations = entry.getValue();

                    List<Integer> seatNumbers = reservations.stream()
                            .map(ReservationEntity::getSeatNumber)
                            .collect(Collectors.toList());

                    ReservationRequestDto dto = ReservationRequestDto.builder()
                            .id(userId)
                            .flightId(reservations.get(0).getFlight().getId())
                            .reservationTime(reservations.get(0).getReservationTime())
                            .seatNumber(seatNumbers)
                            .bookingStatus(reservations.get(0).getBookingStatus())
                            .build();
                    return dto;
                })
                .collect(Collectors.toList());
        return reservationDtos;
    }

    @Override
    public Boolean save(ReservationRequestDto reservationDto) {
        FlightEntity flightEntity= flightRepository.findById(reservationDto.getFlightId()).get();
        if(flightEntity==null){
            throw new FlightNotFoundException("cant find flight");
        }
        if(flightEntity.getSeats()==0 || flightEntity.getSeats()<reservationDto.getSeatNumber().size()){
            return false;
        }
        for(var item : reservationDto.getSeatNumber()){
            ReservationEntity reservationEntity = reservationConverter.convertToEntity(reservationDto,item);
            reservationRepository.save(reservationEntity);
        }
        flightEntity.setSeats(flightEntity.getSeats()-reservationDto.getSeatNumber().size());
        flightRepository.save(flightEntity);
        return true;
    }

    @Override
    public List<ReservationDto> findAllReservations() {
        List<ReservationEntity> reservationEntities = reservationRepository.findAll();
        List<ReservationDto> reservationDtos = reservationEntities.stream()
                .map(item -> reservationConverter.convertToDto(item))
                .collect(Collectors.toList());
        return reservationDtos;
    }

    @Override
    public void delete(Long id) {
        ReservationEntity reservationEntity = reservationRepository.findById(id).get();
        if(reservationEntity==null){
            throw new ReservationNotFoundException("cant find reservation");
        }
        reservationRepository.delete(reservationEntity);
    }

}
