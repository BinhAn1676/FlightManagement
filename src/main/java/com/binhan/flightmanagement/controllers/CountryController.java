package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.CountryDto;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.response.APIResponse;
import com.binhan.flightmanagement.models.CountryEntity;
import com.binhan.flightmanagement.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/country")
public class CountryController {
    private CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService){
        this.countryService=countryService;
    }

    @GetMapping
    public ResponseEntity<List<CountryEntity>> getAllCountries(){
        return ResponseEntity.ok(countryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CountryEntity> getCountry(@PathVariable("id")Long id){
        return ResponseEntity.ok(countryService.findById(id));
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCountry(@PathVariable("id") Long id){
        countryService.delete(id);
        return ResponseEntity.ok("Deleted successfully");
    }

    @PostMapping("/upload-country-data")
    public ResponseEntity<?> uploadCountriesData(@RequestParam("file")MultipartFile file){
        //countryService.saveCountriesByExcel(file);
        return ResponseEntity.ok(Map.of("Message","Countries data uploaded successfully"));
    }

    @GetMapping("/sortField")
    private ResponseEntity<?> getCountriesWithSort(@RequestParam(value = "field",required = false) String field) {
        List<CountryDto> countryDtos = countryService.findCountriesWithSorting(field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(countryDtos.size(), countryDtos));
    }


    @GetMapping("/paginationAndSort")
    private ResponseEntity<?> getCountriesWithPaginationAndSort(@RequestParam("offset") int offset,
                                                            @RequestParam("pageSize") int pageSize,
                                                            @RequestParam(value = "field",required = false) String field) {
        Page<CountryDto> countryDtos = countryService.findCountriesWithPaginationAndSorting(offset, pageSize, field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(countryDtos.getSize(), countryDtos));
    }

}
