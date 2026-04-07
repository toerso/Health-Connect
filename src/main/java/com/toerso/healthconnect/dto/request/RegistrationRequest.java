package com.toerso.healthconnect.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationRequest {
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Username is required")
    private String username;

    @NotNull(message = "Age is required")
    private Integer age;

    @NotBlank
    @Pattern(
            regexp = "^(?:\\+88|88)?01[3-9]\\d{8}$",
            message = "Please enter a  valid mobile number"
    )
    private String phone;

    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Please provide a valid email address"
    )
    private String email;

    @NotBlank
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=]).*$",
            message = "Password must contain uppercase, lowercase, number, and special character"
    )
    private String password;

    private String specialization;
}
