package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.UserConverter;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.models.RoleEntity;
import com.binhan.flightmanagement.models.UserEntity;
import com.binhan.flightmanagement.repository.RoleRepository;
import com.binhan.flightmanagement.repository.UserRepository;
import com.binhan.flightmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private UserConverter userConverter;
    @Autowired
    public UserServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository,UserConverter userConverter){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userConverter = userConverter;
    }

    @Override
    @Transactional
    public UserDto saveUser(RegisterDto userRequest) {
        if (userRepository.existsByUserName(userRequest.getUsername())) {
            return null;
        }
        UserEntity user = new UserEntity();
        user.setUserName(userRequest.getUsername());
        user.setPhone(userRequest.getPhone());
        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        //add role
        RoleEntity roles = roleRepository.findByCode("CUSTOMER").get();
        Set<RoleEntity> roleEntities = user.getRoles();
        roleEntities.add(roles);
        user.setRoles(roleEntities);
        UserEntity userEntity = userRepository.save(user);
        UserDto userDto = userConverter.convertToDto(userEntity);
        return userDto;
    }
}
