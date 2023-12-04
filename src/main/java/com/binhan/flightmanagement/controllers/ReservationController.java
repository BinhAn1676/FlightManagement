package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.ReservationDto;
import com.binhan.flightmanagement.dto.request.ReservationRequestDto;

import com.binhan.flightmanagement.security.JwtService;
import com.binhan.flightmanagement.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {
    private ReservationService reservationService;
    private JwtService jwtService;

    @Autowired
    public ReservationController(ReservationService reservationService,JwtService jwtService) {
        this.reservationService = reservationService;
        this.jwtService=jwtService;
    }

    @GetMapping()
    public ResponseEntity<?> getReservations() {
        List<ReservationRequestDto> reservationDtos = reservationService.findAll();
        return ResponseEntity.ok(reservationDtos);
    }


    @PostMapping
    public ResponseEntity<?> makeReservation(@RequestHeader("Authorization") String token,
                                             @RequestBody ReservationRequestDto reservationDto) {
        String username = jwtService.extractUsername(token);
        reservationDto.setUsername(username);
        Boolean check = reservationService.save(reservationDto);
        if(check){
            return ResponseEntity.ok("save successfully");
        }
        return ResponseEntity.ok("no more avaible seats");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReservation(@PathVariable("id") Long id) {
        reservationService.delete(id);
        return ResponseEntity.ok("delete successfully");
    }
}
