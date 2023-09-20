package com.binhan.flightmanagement.controllers;

import com.binhan.flightmanagement.dto.RoleDto;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class RoleController {
    private RoleService roleService;
    @Autowired
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @PostMapping()
    public ResponseEntity<String> addRole(@RequestBody RoleDto roleDto){
        RoleDto roleSave = roleService.saveRole(roleDto);
        if (roleSave == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Role đã tồn tại");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body("Tạo Role thành công");
        }
    }

    @GetMapping()
    public ResponseEntity<List<RoleDto>> findAll(){
        List<RoleDto> roles = roleService.findAll();
        return new ResponseEntity<>(roles,HttpStatus.OK);
    }
}
