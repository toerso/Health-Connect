package com.toerso.healthconnect.repository;

import com.toerso.healthconnect.contract.MedicalRecordProjection;
import com.toerso.healthconnect.entity.MedicalRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {

    @EntityGraph(attributePaths = {"patient", "doctor", "prescriptions"})
    Page<MedicalRecordProjection> findByPatientId(Long patientId, Pageable pageable);
}
