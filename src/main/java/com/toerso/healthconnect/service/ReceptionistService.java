package com.toerso.healthconnect.service;

import com.toerso.healthconnect.dto.request.AppointmentCreateRequest;
import com.toerso.healthconnect.dto.request.PatientCreateRequest;
import com.toerso.healthconnect.dto.response.AppointmentCreatedResponse;
import com.toerso.healthconnect.dto.response.PatientCreatedResponse;
import jakarta.transaction.Transactional;

public interface ReceptionistService {

    public PatientCreatedResponse createPatient(PatientCreateRequest request);

    public AppointmentCreatedResponse scheduleAppointment(AppointmentCreateRequest request);
}
