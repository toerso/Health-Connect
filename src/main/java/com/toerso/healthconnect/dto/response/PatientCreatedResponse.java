package com.toerso.healthconnect.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCreatedResponse {
    private Long patientId;
    private String temporaryPassword;
}
