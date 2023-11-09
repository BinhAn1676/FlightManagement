package com.binhan.flightmanagement.controllers.auth;

import com.binhan.flightmanagement.dto.AuthResponseDto;
import com.binhan.flightmanagement.dto.EmailMessage;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.LoginDto;
import com.binhan.flightmanagement.dto.request.NewPassword;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.security.JWTGenerator;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTGenerator jwtGenerator;
    private EmailSenderService emailSenderService;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,UserService userService,
                          JWTGenerator jwtGenerator,EmailSenderService emailSenderService){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> Register(@RequestBody @Valid RegisterDto userRequest){
        UserDto userDto = userService.saveUser(userRequest);
        if (userDto == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản đã tồn tại");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body("Tạo tài khoản thành công");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDto(token), HttpStatus.OK);
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
