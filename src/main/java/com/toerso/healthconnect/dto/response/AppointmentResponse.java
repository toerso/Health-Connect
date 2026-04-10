package com.toerso.healthconnect.dto.response;

import com.toerso.healthconnect.enums.AppointmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {
    private Long id;
    private String doctorName;
    private String patientName;
    private LocalDateTime appointmentTime;
    private String appointmentStatus;
}
