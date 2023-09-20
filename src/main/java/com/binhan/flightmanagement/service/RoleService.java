package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.RoleDto;

import java.util.List;

public interface RoleService {
    RoleDto saveRole(RoleDto roleDto);

    List<RoleDto> findAll();
}
