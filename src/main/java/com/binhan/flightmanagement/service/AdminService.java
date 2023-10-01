package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.UserDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {

    List<UserDto> findAll();

    List<UserDto> findUsersWithSorting(String field);

    Page<UserDto> findUsersWithPagination(int offset, int pageSize);

    Page<UserDto> findUsersWithPaginationAndSorting(int offset, int pageSize, String field);
}