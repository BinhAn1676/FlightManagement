package com.binhan.flightmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    private Long id;
    private String oldPassword;
    private String newPassword;
    private String repeatNewPassword;
}
