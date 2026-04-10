package com.toerso.healthconnect.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordResponse {
    private Long id;
    private String patientName;
    private String doctorName;
    private String symptoms;
    private String diagnosis;
    private LocalDateTime lastVisitDate;
    private List<PrescriptionResponse> prescriptionList;
}
