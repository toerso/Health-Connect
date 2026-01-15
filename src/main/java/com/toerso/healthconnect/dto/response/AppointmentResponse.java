package com.toerso.healthconnect.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private String doctorName;
    private String patientName;
    private LocalDateTime appointmentTime;
    private String status;
}
