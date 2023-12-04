package com.binhan.flightmanagement.dto;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String fullName;
    private String userName;
    private String password;
    private String phone;
    @Lob
    private byte[] imageData;
    private String email;
}
