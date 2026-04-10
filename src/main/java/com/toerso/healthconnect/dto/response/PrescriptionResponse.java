package com.toerso.healthconnect.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrescriptionResponse {
    private Long id;
    private String medicine;
    private String efficacy;
    private String dosage;
    private String duration;
    private String instructions;
}