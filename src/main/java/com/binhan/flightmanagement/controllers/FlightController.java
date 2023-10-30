package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.FlightDto;
import com.binhan.flightmanagement.models.FlightEntity;
import com.binhan.flightmanagement.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flight")
public class FlightController {

    private FlightService flightService;
    
    @Autowired
    public FlightController(FlightService flightService){
        this.flightService= flightService;
    }

    @GetMapping
    public ResponseEntity<List<FlightEntity>> getAllFlights(){
        return ResponseEntity.status(HttpStatus.OK).body(flightService.findAll());
    }

    @PostMapping
    public ResponseEntity<String> addFlight(@RequestBody FlightDto newFlight){

        return ResponseEntity.status(HttpStatus.OK).body("add successfully");
    }
}
