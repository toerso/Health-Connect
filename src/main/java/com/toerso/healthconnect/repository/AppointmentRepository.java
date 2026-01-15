package com.toerso.healthconnect.repository;

import com.toerso.healthconnect.entity.Appointment;
import com.toerso.healthconnect.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    public boolean existsByDoctorAndAppointmentTime(Doctor doctor, LocalDateTime appointmentTime);
}
