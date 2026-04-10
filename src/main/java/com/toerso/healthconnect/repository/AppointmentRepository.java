package com.toerso.healthconnect.repository;

import com.toerso.healthconnect.entity.Appointment;
import com.toerso.healthconnect.entity.Doctor;
import com.toerso.healthconnect.entity.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime appointmentTime);

    Page<Appointment> findByPatient(Patient patient, Pageable pageable);

    Page<Appointment> findByDoctor(Doctor doctor, Pageable pageable);

    Page<Appointment> findByDoctorId(Long doctorId, Pageable pageable);
}
