package com.toerso.healthconnect.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record EncounterRequest(
        @NotBlank(message = "Symptoms cannot be empty")
        @Size(max = 2000)
        String symptoms,

        @NotBlank(message = "Diagnosis is required")
        @Size(max = 2000)
        String diagnosis,

        @NotEmpty(message = "At least one prescription is required or mark as 'None'")
        List<PrescriptionRequest> prescriptions
) {
    public record PrescriptionRequest(
            @NotBlank(message = "Medicine name is required")
            String medicine,

            @NotBlank(message = "Medicine's efficacy is required")
            String efficacy,

            @NotBlank(message = "Medicine's dosage is required")
            String dosage,

            @NotBlank(message = "Medicine's duration is required")
            String duration,

            String instructions
    ){};
}