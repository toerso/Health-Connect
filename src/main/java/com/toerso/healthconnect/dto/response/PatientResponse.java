package com.toerso.healthconnect.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PatientResponse {
    private Long id;
    private String name;
    private String phone;
}
