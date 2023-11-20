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
    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @GetMapping
    public ResponseEntity<List<FlightEntity>> getAllFlights() {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<FlightEntity> getFlight(@PathVariable("id")Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(flightService.findById(id));
    }

    @PostMapping
    public ResponseEntity<?> addFlight(@RequestBody FlightDto newFlight) {
        FlightEntity flightEntity = flightService.save(newFlight);
        return ResponseEntity.status(HttpStatus.OK).body(flightEntity);
    }

    @PutMapping
    public ResponseEntity<?> updateFlight(@RequestBody FlightDto newFlight) {
        FlightEntity flightEntity = flightService.save(newFlight);
        return ResponseEntity.status(HttpStatus.OK).body(flightEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFlight(@PathVariable("id") Long id){
        flightService.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }
}
