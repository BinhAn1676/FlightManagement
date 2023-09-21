package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.ChangePasswordDto;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDto saveUser(RegisterDto userRequest);

    List<UserDto> findAll();

    String update(UserDto userDto) ;

    UserDto findById(Long id);

    String uploadImage(MultipartFile file,Long id) throws IOException;

    byte[] findImageById(Long id);

    String changePassword(ChangePasswordDto changePasswordDto);
}
