package com.toerso.healthconnect.contract;

import com.toerso.healthconnect.entity.Prescription;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import java.util.List;

public interface MedicalRecordProjection {
    Long getId();
    String getSymptoms();
    String getDiagnosis();
    LocalDateTime getVisitDate();

    // SpEL (Spring Expression Language) to flatten the relationship
    @Value("#{target.patient.name}")
    String getPatientName();

    @Value("#{target.doctor.name}")
    String getDoctorName();

    // Fetches the associated collection
    List<Prescription> getPrescriptions();
}
