package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.AuthResponseDto;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.LoginDto;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.dto.response.ResponseDto;
import com.binhan.flightmanagement.security.JWTGenerator;
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
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JWTGenerator jwtGenerator;
    @Autowired
    public AuthController(AuthenticationManager authenticationManager,UserService userService,JWTGenerator jwtGenerator){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
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
}
