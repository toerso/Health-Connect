package com.toerso.healthconnect.service;

import com.toerso.healthconnect.dto.request.PatientCreateRequest;
import com.toerso.healthconnect.dto.response.PatientResponse;

public interface PatientService {
    PatientResponse createPatient(PatientCreateRequest request);
}
