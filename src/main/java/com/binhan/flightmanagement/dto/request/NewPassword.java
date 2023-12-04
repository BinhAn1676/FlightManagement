package com.binhan.flightmanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;



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
