package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.ReservationDto;
import com.binhan.flightmanagement.dto.request.ReservationRequestDto;

import java.util.List;

public interface ReservationService {
    List<ReservationRequestDto> findAll();

    Boolean save(ReservationRequestDto reservationDto);

    List<ReservationDto> findAllReservations();
}
