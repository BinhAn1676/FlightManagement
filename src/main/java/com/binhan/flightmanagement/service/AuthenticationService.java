package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.request.AuthenticationRequest;
import com.binhan.flightmanagement.dto.request.RegisterRequest;
import com.binhan.flightmanagement.dto.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse register(RegisterRequest request);
}
