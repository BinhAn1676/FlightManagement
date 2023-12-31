package com.binhan.flightmanagement.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    @NotNull(message = "username shouldn't be null")
    private String username;

    private String fullName;

    @Email(message = "invalid email address")
    private String email;
    @Size(min = 6, message = "Password must be at least 6 characters long")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).*$", message = "Password must contain at least one uppercase letter and one digit")
    private String password;

    @NotNull(message = "cant be empty")
    private String repeatPassword;

    @Pattern(regexp = "^\\d{10}$",message = "invalid mobile number entered ")
    @Nullable
    private String phone;
}
