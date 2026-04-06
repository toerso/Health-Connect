package com.toerso.healthconnect.dto.response;

import com.toerso.healthconnect.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationResponse {
    private Long id;
    private String username;
    private String message;
}
