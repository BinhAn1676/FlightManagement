package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.AircraftDto;
import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.dto.response.APIResponse;
import com.binhan.flightmanagement.models.AircraftEntity;
import com.binhan.flightmanagement.service.AircraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aircraft")
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getAircraft(@PathVariable("id")Long id){
        return ResponseEntity.ok(aircraftService.findById(id));
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

    @GetMapping("/sortField")
    private ResponseEntity<?> getAirportsWithSort(@RequestParam(value = "field",required = false) String field) {
        List<AircraftDto> aircraftDtos = aircraftService.findAircraftsWithSorting(field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(aircraftDtos.size(), aircraftDtos));
    }


    @GetMapping("/paginationAndSort")
    private ResponseEntity<?> getAirportsWithPaginationAndSort(@RequestParam("offset") int offset,
                                                               @RequestParam("pageSize") int pageSize,
                                                               @RequestParam(value = "field",required = false) String field) {
        Page<AircraftDto> aircraftDtos = aircraftService.findAircraftsWithPaginationAndSorting(offset, pageSize, field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(aircraftDtos.getSize(), aircraftDtos));
    }

}
