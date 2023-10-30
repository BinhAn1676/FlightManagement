package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryController {
    private CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService){
        this.countryService=countryService;
    }

    @GetMapping
    public ResponseEntity<List<CountryEntity>> getAllCountry(){
        return ResponseEntity.ok(countryService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> addCountry(@RequestBody CountryDto newCountry){
        CountryEntity countrySaved = countryService.save(newCountry);
        if(countrySaved!=null){
            return ResponseEntity.ok(countrySaved);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @PutMapping
    public ResponseEntity<?> updateCountry(@RequestBody CountryDto country){
        CountryEntity countrySaved = countryService.save(country);
        if(countrySaved!=null){
            return ResponseEntity.ok(countrySaved);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

}
