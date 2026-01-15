package com.toerso.healthconnect.service;

import com.toerso.healthconnect.dto.request.AppointmentCreateRequest;
import com.toerso.healthconnect.dto.response.AppointmentResponse;

public interface AppointmentService {
    AppointmentResponse bookAppointment(AppointmentCreateRequest request);
}
