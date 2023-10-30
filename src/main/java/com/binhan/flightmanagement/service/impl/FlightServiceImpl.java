package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.repository.FlightRepository;
import com.binhan.flightmanagement.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightServiceImpl implements FlightService {

    private FlightRepository flightRepository;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository){
        this.flightRepository=flightRepository;
    }

    @Override
    public List<FlightEntity> findAll() {
        return flightRepository.findAll();
    }
}
