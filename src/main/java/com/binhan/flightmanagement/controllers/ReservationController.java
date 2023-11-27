package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.ReservationDto;
import com.binhan.flightmanagement.dto.request.ReservationRequestDto;
import com.binhan.flightmanagement.security.JWTGenerator;
import com.binhan.flightmanagement.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private ReservationService reservationService;
    private JWTGenerator jwtGenerator;

    @Autowired
    public ReservationController(ReservationService reservationService,JWTGenerator jwtGenerator) {
        this.reservationService = reservationService;
        this.jwtGenerator=jwtGenerator;
    }

    @GetMapping()
    public ResponseEntity<?> getReservations() {
        List<ReservationRequestDto> reservationDtos = reservationService.findAll();
        return ResponseEntity.ok(reservationDtos);
    }


    @PostMapping
    public ResponseEntity<?> makeReservation(@RequestHeader("Authorization") String token,
                                             @RequestBody ReservationRequestDto reservationDto) {
        String username = jwtGenerator.getUsernameFromJWT(token);
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
