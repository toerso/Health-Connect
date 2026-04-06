package com.toerso.healthconnect.dto.request;

import com.toerso.healthconnect.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Set;

@Data
public class InvitationRequest {
    @NotBlank(message = "Email is required")
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Please provide a valid email address"
    )
    private String email;

    @NotEmpty(message = "Role is Required")
    private Set<RoleType> roles;
}
