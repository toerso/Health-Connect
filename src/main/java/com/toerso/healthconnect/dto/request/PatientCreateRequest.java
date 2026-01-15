package com.toerso.healthconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PatientCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "Phone must be 11 digits")
    private String phone;

    @NotNull
    private LocalDate dateOfBirth;

    private String medicalHistory;
}
