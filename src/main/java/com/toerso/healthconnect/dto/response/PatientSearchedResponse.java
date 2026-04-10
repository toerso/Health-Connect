package com.toerso.healthconnect.dto.response;

import com.toerso.healthconnect.entity.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientSearchedResponse {
    private Long id;
    private String name;
    private Integer age;
    private String phone;
}
