package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.UserConverter;
import com.binhan.flightmanagement.dto.RoleDto;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.ChangePasswordDto;
import com.binhan.flightmanagement.dto.request.NewPassword;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.dto.request.RegisterRequest;
import com.binhan.flightmanagement.exception.RoleNotFoundException;
import com.binhan.flightmanagement.exception.UserNotFoundException;
import com.binhan.flightmanagement.exception.WrongOldPasswordException;
import com.binhan.flightmanagement.exception.WrongRepeatPasswordException;
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

import javax.management.relation.Role;
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

    /*@Override
    public UserDto saveUser(RegisterDto userRequest) {
        if (userRepository.existsByUserName(userRequest.getUsername())) {//check user is already existed
            return null;
        }
        if(!userRequest.getPassword().equals(userRequest.getRepeatPassword())){//wrong repeat password
            throw new WrongRepeatPasswordException("wrong repeat password");
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
    }*/


    @Override
    public String update(UserDto userDto) {
        UserEntity user = userRepository.findById(userDto.getId()).get();
        if (user == null) {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found");
        }
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
        if (user == null) {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found");
        }
        UserDto userDto = userConverter.convertToDto(user);
        userDto.setImageData(null);
        return userDto;
    }

    @Override
    public String uploadImage(MultipartFile file,Long id) throws IOException {
        UserEntity userEntity = userRepository.findById(id).get();
        if (userEntity == null) {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found");
        }
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
        if (user == null) {
            // Handle the case where the user is not found
            throw new UserNotFoundException("User not found");
        }
        String storedPassword = user.getPassword();
        String enteredOldPassword = changePasswordDto.getOldPassword();

        if (passwordEncoder.matches(enteredOldPassword, storedPassword)) {
            if(!changePasswordDto.getNewPassword().equals(changePasswordDto.getRepeatNewPassword())){
                throw new WrongRepeatPasswordException("wrong repeat password");
            }else{
                user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
                userRepository.save(user);
                return "Change password successfully";
            }
        } else {
            throw new WrongOldPasswordException("Wrong old password");
        }
    }

    @Override
    public String saveNewUser(RegisterRequest newUser) {
        if (userRepository.existsByUserName(newUser.getUsername())) {//check user is already existed
            return null;
        }
        UserEntity user = new UserEntity();
        user.setUserName(newUser.getUsername());
        user.setPhone(newUser.getPhone());
        user.setEmail(newUser.getEmail());
        user.setFullName(newUser.getFullName());
        user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        UserEntity userEntity = userRepository.save(user);
        if(userEntity!=null){
            return "Add user Successfully";
        }
        return "add failed";

    }

    @Override
    public String addRoletoUser(Long userId, RoleDto roleDto) {
        UserEntity user = userRepository.findById(userId).get();
        if(user == null){
            throw new UserNotFoundException("Cant find this user id");
        }
        RoleEntity roleEntity = roleRepository.findByCode(roleDto.getCode()).get();
        if(roleEntity == null){
            throw new RoleNotFoundException("Cant find this role");
        }
        Set<RoleEntity> roles = user.getRoles();
        roles.add(roleEntity);
        user.setRoles(roles);
        UserEntity userSave = userRepository.save(user);
        if(userSave != null){
            return "add role successfully to user";
        }
        return "add failed";
    }

    @Override
    public void forgetPassword(NewPassword newPassword) {
        UserEntity user = userRepository.findByEmail(newPassword.getGmail())
                .orElseThrow(() -> new UserNotFoundException("Cant find this account"));
        if(!newPassword.getNewPassword().equals(newPassword.getRepeatNewPassword())){
            throw new WrongRepeatPasswordException("wrong repeat password");
        }else{
            user.setPassword(passwordEncoder.encode(newPassword.getNewPassword()));
            userRepository.save(user);
        }
    }
}
