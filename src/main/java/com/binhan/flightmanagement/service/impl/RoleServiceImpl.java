package com.binhan.flightmanagement.service.impl;

import com.binhan.flightmanagement.converter.RoleConverter;
import com.binhan.flightmanagement.dto.RoleDto;
import com.binhan.flightmanagement.models.RoleEntity;
import com.binhan.flightmanagement.repository.RoleRepository;
import com.binhan.flightmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;
    private RoleConverter roleConverter;
    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,RoleConverter roleConverter){
        this.roleRepository= roleRepository;
        this.roleConverter = roleConverter;
    }

    @Override
    public RoleDto saveRole(RoleDto roleDto) {
        if(roleRepository.existsByCode(roleDto.getCode())){
            return null;
        }
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setCode(roleDto.getCode());
        roleEntity.setName(roleDto.getName());
        roleEntity = roleRepository.save(roleEntity);
        RoleDto roleSaved = new RoleDto();
        roleSaved.setCode(roleEntity.getCode());
        roleSaved.setName(roleEntity.getName());
        return roleSaved;
    }

    @Override
    public List<RoleDto> findAll() {
        List<RoleDto> roleDtos= new ArrayList<>();
        List<RoleEntity> roleEntities = roleRepository.findAll();
        for (var item:roleEntities) {
            RoleDto role = roleConverter.convertToDto(item);
            roleDtos.add(role);
        }
        return roleDtos;
    }


}
