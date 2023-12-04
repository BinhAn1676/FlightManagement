package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.dto.request.AuthenticationRequest;
import com.binhan.flightmanagement.dto.request.RegisterRequest;
import com.binhan.flightmanagement.dto.response.AuthenticationResponse;
import com.binhan.flightmanagement.models.RoleEntity;
import com.binhan.flightmanagement.models.UserEntity;
import com.binhan.flightmanagement.repository.RoleRepository;
import com.binhan.flightmanagement.repository.UserRepository;
import com.binhan.flightmanagement.security.JwtService;
import com.binhan.flightmanagement.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    //private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByUserName(request.getUsername())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        //var refreshToken = jwtService.generateRefreshToken(user);
        //revokeAllUserTokens(user);
        //saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                //.refreshToken(refreshToken)
                .build();
    }

    @Override
    public AuthenticationResponse register(RegisterRequest request) {
        RoleEntity roles = roleRepository.findByCode("CUSTOMER").get();
        Set<RoleEntity> roleEntities = new HashSet<>();
        roleEntities.add(roles);
        var user = UserEntity.builder()
                .userName(request.getUsername())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .roles(roleEntities)
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        //var refreshToken = jwtService.generateRefreshToken(user);
        //saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                //.refreshToken(refreshToken)
                .build();
    }
}
