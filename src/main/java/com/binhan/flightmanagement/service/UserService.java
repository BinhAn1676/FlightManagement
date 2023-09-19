package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.RegisterDto;

public interface UserService {
    UserDto saveUser(RegisterDto userRequest);
}
