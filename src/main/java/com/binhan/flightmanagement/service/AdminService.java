package com.binhan.flightmanagement.service;

import com.binhan.flightmanagement.dto.ReservationDto;
import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.response.BaseResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {

    List<UserDto> findAll();

    List<UserDto> findUsersWithSorting(String field);

    Page<UserDto> findUsersWithPagination(int offset, int pageSize);

    Page<UserDto> findUsersWithPaginationAndSorting(int offset, int pageSize, String field);

    BaseResponse importData(MultipartFile importFile);

    Page<ReservationDto> findReservationsWithPaginationAndSorting(int offset, int pageSize, String field);

    List<ReservationDto> findReservationsWithSorting(String field);
}
