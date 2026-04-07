package com.toerso.healthconnect.entity;


import com.toerso.healthconnect.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(name = "appointment_time")
    private LocalDateTime appointmentTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "appointment_status")
    private AppointmentStatus appointmentStatus;

    @Version
    private Long version;
}
