package com.binhan.flightmanagement.converter;

import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.models.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {
    public UserDto convertToDto(UserEntity user){
        UserDto userDto = new UserDto();
        userDto.setUserName(user.getUserName());
        userDto.setPhone(user.getPhone());
        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
