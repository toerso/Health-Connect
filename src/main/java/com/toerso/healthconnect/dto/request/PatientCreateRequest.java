package com.toerso.healthconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCreateRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String username;

    @NotNull
    private Integer age;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "Phone must be 11 digits")
    private String phone;

    private String medicalHistory;
}
