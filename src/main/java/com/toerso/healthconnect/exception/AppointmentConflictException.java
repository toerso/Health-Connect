package com.toerso.healthconnect.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppointmentConflictException extends RuntimeException{
    public AppointmentConflictException(Long doctorId, LocalDateTime time) {
        super(String.format(
                "Doctor %d has already an appointment at %s",
                doctorId,
                time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        ));
    }
}
