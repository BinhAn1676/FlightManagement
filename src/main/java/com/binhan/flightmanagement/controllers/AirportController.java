package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/airport")
public class AirportController {
    private AirportService airportService;

    @Autowired
    public AirportController(AirportService airportService){
        this.airportService=airportService;
    }

    @GetMapping
    public ResponseEntity<List<AirportEntity>> getAllAirports(){
        return ResponseEntity.status(HttpStatus.OK).body(airportService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> addAirport(@RequestBody AirportDto airport){
        AirportEntity airportEntity = airportService.save(airport);
        if(airportEntity != null){
            return ResponseEntity.ok(airportEntity);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateAirport(@RequestBody AirportDto airport){
        AirportEntity airportEntity = airportService.save(airport);
        if(airportEntity != null){
            return ResponseEntity.ok(airportEntity);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAirport(@PathVariable("id") Long id){
        airportService.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("/image")
    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = airportService.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData=airportService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }

}
