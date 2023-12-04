package com.binhan.flightmanagement.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChangePasswordDto {

    private Long id;
    private String oldPassword;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter and one digit")
    private String newPassword;

    @NotNull(message = "cant be empty")
    private String repeatNewPassword;
}
