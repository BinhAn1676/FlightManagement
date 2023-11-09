package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.UserConverter;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.response.BaseResponse;
import com.binhan.flightmanagement.models.UserEntity;
import com.binhan.flightmanagement.repository.UserRepository;
import com.binhan.flightmanagement.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
@Service
public class AdminServiceImpl implements AdminService {

    private UserRepository userRepository;
    private UserConverter userConverter;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository,UserConverter userConverter){
        this.userRepository=userRepository;
        this.userConverter=userConverter;
    }

    @Override
    public List<UserDto> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(var item : userEntities){
            UserDto userDto = userConverter.convertToDto(item);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public List<UserDto> findUsersWithSorting(String field) {
        List<UserEntity> userEntities;
        if(field == null){
            userEntities = userRepository.findAll();
        }else{
            userEntities = userRepository.findAll(Sort.by(Sort.Direction.ASC,field));
        }
        List<UserDto> userDtos = new ArrayList<>();
        for(var item : userEntities){
            UserDto userDto = userConverter.convertToDto(item);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public Page<UserDto> findUsersWithPagination(int offset, int pageSize) {
        Page<UserEntity> userEntities = userRepository.findAll(PageRequest.of(offset, pageSize));
        Page<UserDto> userDtos = userEntities.map(user -> {
            UserDto userDto = new UserDto();
            userDto.setId(user.getId());
            userDto.setUserName(user.getUserName());
            userDto.setEmail(user.getEmail());
            userDto.setFullName(user.getFullName());
            userDto.setPhone(user.getPhone());
            userDto.setPassword(user.getPassword());
            return userDto;
        });
        return userDtos;
    }

    @Override
    public Page<UserDto> findUsersWithPaginationAndSorting(int offset, int pageSize, String field) {
        Page<UserEntity> userEntities;

        if (field == null) {
            userEntities = userRepository.findAll(PageRequest.of(offset, pageSize));
        } else {
            userEntities = userRepository.findAll(PageRequest.of(offset, pageSize).withSort(Sort.by(field)));
        }

        return userEntities.map(this::mapToUserDto);
    }

    @Override
    public BaseResponse importData(MultipartFile importFile) {
        return null;
    }

    private UserDto mapToUserDto(UserEntity user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUserName(user.getUserName());
        userDto.setEmail(user.getEmail());
        userDto.setFullName(user.getFullName());
        userDto.setPhone(user.getPhone());
        userDto.setPassword(user.getPassword());
        return userDto;
    }
}
