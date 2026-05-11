package com.toerso.healthconnect.service;

import com.toerso.healthconnect.dto.request.EncounterRequest;
import com.toerso.healthconnect.dto.response.AppointmentResponse;
import com.toerso.healthconnect.dto.response.MedicalRecordResponse;
import com.toerso.healthconnect.dto.response.PatientSearchedResponse;
import com.toerso.healthconnect.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    Page<AppointmentResponse> getDoctorSchedule(User doctor, Pageable pageable);
    Page<PatientSearchedResponse> searchPatients(User doctor, String name, String phone, Pageable pageable);

    Page<MedicalRecordResponse> getPatientMedicalHistory(User doctor, Long patientId, Pageable pageable);
    void createEncounter(User user, Long patientId, EncounterRequest request);
}
