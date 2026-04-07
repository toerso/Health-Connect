package com.toerso.healthconnect.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AppointmentCreatedResponse {
    private Long serialNo;
    private String doctorName;
    private String patientName;
    private LocalDateTime appointmentTime;
    private String status;
}
