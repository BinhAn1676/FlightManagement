package com.binhan.flightmanagement.controllers.admin;

import com.binhan.flightmanagement.dto.UserDto;
import com.binhan.flightmanagement.dto.request.RegisterDto;
import com.binhan.flightmanagement.dto.response.APIResponse;
import com.binhan.flightmanagement.service.AdminService;
import com.binhan.flightmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private UserService userService;

    private AdminService adminService;

    @Autowired
    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    /**
     * admin can add more user which can be employee
     *
     * @param newUser
     * @return
     */
    @PostMapping("/newUser")
    public ResponseEntity<?> addNewUser(@RequestBody RegisterDto newUser) {
        String msg = userService.saveNewUser(newUser);
        if (msg == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Tài khoản đã tồn tại");
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        }
    }

    @GetMapping()
    private ResponseEntity<?> getUsersWithSort(@RequestParam(value = "field",required = false) String field) {
        List<UserDto> userDtos = adminService.findUsersWithSorting(field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(userDtos.size(), userDtos));
    }


    @GetMapping("/paginationAndSort")
    private ResponseEntity<?> getUsersWithPaginationAndSort(@RequestParam("offset") int offset,
                                                            @RequestParam("pageSize") int pageSize,
                                                            @RequestParam(value = "field",required = false) String field) {
        Page<UserDto> usersPaging = adminService.findUsersWithPaginationAndSorting(offset, pageSize, field);
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(usersPaging.getSize(), usersPaging));
    }

}
