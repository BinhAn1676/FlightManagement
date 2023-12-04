package com.binhan.flightmanagement.controllers.auth;

import com.binhan.flightmanagement.dto.AuthResponseDto;
import com.binhan.flightmanagement.dto.EmailMessage;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.*;
import com.binhan.flightmanagement.dto.response.AuthenticationResponse;
import com.binhan.flightmanagement.service.AuthenticationService;
import com.binhan.flightmanagement.service.EmailSenderService;
import com.binhan.flightmanagement.service.UserService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private EmailSenderService emailSenderService;
    private AuthenticationService authenticationService;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,UserService userService,
                          EmailSenderService emailSenderService,
                          AuthenticationService authenticationService){
        this.authenticationManager = authenticationManager;
        this.authenticationService=authenticationService;
        this.userService = userService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping()
    public ResponseEntity<?> sendMailForgetPassword(@RequestBody EmailMessage emailMessage){
        this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/checkNumber")
    public ResponseEntity<String> checkNumber(@RequestBody Integer numberToCheck) {
        boolean isMatch = emailSenderService.checkNumber(numberToCheck);

        if (isMatch) {
            // Return a URL to a different page
            String url = "http://localhost:8080/auth/newPassword";
            return ResponseEntity.status(HttpStatus.OK).body(url);
        } else {
            // Handle the case where the number doesn't match
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Number not found");
        }
    }

    @PutMapping("/newPassword")
    public ResponseEntity<String> changePassword(@RequestBody NewPassword newPassword){
        userService.forgetPassword(newPassword);
        return ResponseEntity.status(HttpStatus.OK).body("Changed password successfuly");
    }

}
