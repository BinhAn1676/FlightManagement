package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.AircraftDto;
import com.binhan.flightmanagement.models.AircraftEntity;
import com.binhan.flightmanagement.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aircraft")
public class AircraftController {
    private AircraftService aircraftService;

    @Autowired
    public AircraftController(AircraftService aircraftService){
        this.aircraftService=aircraftService;
    }

    @GetMapping
    public ResponseEntity<?> getAircrafts(){
        return ResponseEntity.ok(aircraftService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> addAircraft(@RequestBody AircraftDto aircraftDto){
        AircraftEntity aircraftEntity =aircraftService.save(aircraftDto);
        if(aircraftEntity!=null){
            return ResponseEntity.ok(aircraftEntity);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateAircraft(@RequestBody AircraftDto aircraftDto){
        AircraftEntity aircraftEntity =aircraftService.save(aircraftDto);
        if(aircraftEntity!=null){
            return ResponseEntity.ok(aircraftEntity);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAircraft(@PathVariable("id") Long id){
        aircraftService.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

}
