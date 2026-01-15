package com.toerso.healthconnect.repository;

import com.toerso.healthconnect.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
