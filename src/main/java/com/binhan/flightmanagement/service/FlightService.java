package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.models.FlightEntity;

import java.util.List;

public interface FlightService {
    List<FlightEntity> findAll();
}
