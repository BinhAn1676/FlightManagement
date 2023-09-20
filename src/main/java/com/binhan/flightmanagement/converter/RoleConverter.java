package com.binhan.flightmanagement.converter;

import com.binhan.flightmanagement.dto.RoleDto;
import com.binhan.flightmanagement.models.RoleEntity;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter {
    public RoleDto convertToDto(RoleEntity roleEntity){
        RoleDto roleDto = new RoleDto();
        roleDto.setName(roleEntity.getName());
        roleDto.setCode(roleEntity.getCode());
        return roleDto;
    }
}
