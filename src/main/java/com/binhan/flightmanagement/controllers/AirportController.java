package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.AirportDto;
import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.dto.response.APIResponse;
import com.binhan.flightmanagement.models.AirportEntity;
import com.binhan.flightmanagement.service.AirportService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/airport")
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

    @GetMapping("/{id}")
    public ResponseEntity<AirportEntity> getAirport(@PathVariable("id")Long id){
        return ResponseEntity.status(HttpStatus.OK).body(airportService.findById(id));
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
    @Operation(
            description = "suggest using postman to send image file",
            summary = "Upload airport image "
    )
    @PostMapping("/image")
    public ResponseEntity<?> uploadImageToFIleSystem(@RequestParam("image") MultipartFile file) throws IOException {
        String uploadImage = airportService.uploadImageToFileSystem(file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }
    @Operation(
            description = "suggest using postman to get image file",
            summary = "Get airport image "
    )
    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException {
        byte[] imageData=airportService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);

    }
    @Operation(
            description = "Get all Airports by field sort ASC ",
            summary = "Find Airports with sorting"
    )
    @GetMapping("/sortField")
    private ResponseEntity<?> getAirportsWithSort(@RequestParam(value = "field",required = false) String field) {
        List<AirportDto> airportDtos = airportService.findAirportsWithSorting(field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(airportDtos.size(), airportDtos));
    }

    @Operation(
            description = "Get Airports by field sort ASC and pagination",
            summary = "Find Airports with sorting and pagination"
    )
    @GetMapping("/paginationAndSort")
    private ResponseEntity<?> getAirportsWithPaginationAndSort(@RequestParam("offset") int offset,
                                                                @RequestParam("pageSize") int pageSize,
                                                                @RequestParam(value = "field",required = false) String field) {
        Page<AirportDto> airportDtos = airportService.findAirportsWithPaginationAndSorting(offset, pageSize, field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(airportDtos.getSize(), airportDtos));
    }

}
