package com.toerso.healthconnect.repository;

import com.toerso.healthconnect.entity.Appointment;
import com.toerso.healthconnect.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    public boolean existsByDoctorIdAndAppointmentTime(Long doctorId, LocalDateTime appointmentTime);

    public List<Appointment> findByPatientId(Long patientId);
}
