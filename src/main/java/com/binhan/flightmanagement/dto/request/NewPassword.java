package com.binhan.flightmanagement.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Setter
@Getter
public class NewPassword {

    @Email
    private String gmail;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter and one digit")
    private String newPassword;

    @NotNull(message = "cant be empty")
    private String repeatNewPassword;
}
