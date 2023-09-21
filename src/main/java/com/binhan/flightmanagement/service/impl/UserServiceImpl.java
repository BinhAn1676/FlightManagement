package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.UserConverter;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.ChangePasswordDto;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.models.RoleEntity;
import com.binhan.flightmanagement.models.UserEntity;
import com.binhan.flightmanagement.repository.RoleRepository;
import com.binhan.flightmanagement.repository.UserRepository;
import com.binhan.flightmanagement.service.UserService;
import com.binhan.flightmanagement.util.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@Transactional
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

    @Override
    public List<UserDto> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for(var item : userEntities){
            UserDto userDto = userConverter.convertToDto(item);
            byte[] images=ImageUtils.decompressImage(item.getImageData());
            userDto.setImageData(images);
            userDtos.add(userDto);
        }
        return userDtos;
    }

    @Override
    public String update(UserDto userDto) {
        UserEntity user = userRepository.findById(userDto.getId()).get();
        user.setPhone(userDto.getPhone());
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        UserEntity userSave = userRepository.save(user);
        if (userSave != null) {
            return "user updated successfully !";
        }
        return null;
    }

    @Override
    public UserDto findById(Long id) {
        UserEntity user = userRepository.findById(id).get();
        UserDto userDto = userConverter.convertToDto(user);
        userDto.setImageData(null);
        return userDto;
    }

    @Override
    public String uploadImage(MultipartFile file,Long id) throws IOException {
        UserEntity userEntity = userRepository.findById(id).get();
        userEntity.setImageData(ImageUtils.compressImage(file.getBytes()));
        userEntity = userRepository.save(userEntity);
        if (userEntity != null) {
            return "file uploaded successfully : " + file.getOriginalFilename();
        }
        return null;
    }

    @Override
    public byte[] findImageById(Long id) {
        UserEntity user = userRepository.findById(id).get();
        byte[] image=ImageUtils.decompressImage(user.getImageData());
        return image;
    }

    @Override
    public String changePassword(ChangePasswordDto changePasswordDto) {
        UserEntity user = userRepository.findById(changePasswordDto.getId()).get();

        return null;
    }
}
