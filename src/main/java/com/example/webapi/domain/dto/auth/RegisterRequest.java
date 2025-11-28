package com.example.webapi.domain.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username must not be blank")
    @Pattern(regexp = "^[a-z0-9]{4,50}$", message = "Username must be 4-50 characters, lowercase letters and numbers only")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @jakarta.validation.constraints.Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be a valid email address")
    private String email;
}