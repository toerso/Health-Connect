package com.toerso.healthconnect.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class DoctorScheduleResponse {
    private String patientName;
    private LocalDateTime appointmentTime;
}
