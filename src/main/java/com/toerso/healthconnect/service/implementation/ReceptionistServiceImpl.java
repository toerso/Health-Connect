package com.toerso.healthconnect.service.implementation;

import com.toerso.healthconnect.dto.request.AppointmentCreateRequest;
import com.toerso.healthconnect.dto.request.PatientCreateRequest;
import com.toerso.healthconnect.dto.response.AppointmentCreatedResponse;
import com.toerso.healthconnect.dto.response.PatientCreatedResponse;
import com.toerso.healthconnect.entity.Appointment;
import com.toerso.healthconnect.entity.Doctor;
import com.toerso.healthconnect.entity.Patient;
import com.toerso.healthconnect.entity.User;
import com.toerso.healthconnect.enums.AppointmentStatus;
import com.toerso.healthconnect.enums.RoleType;
import com.toerso.healthconnect.exception.AppointmentConflictException;
import com.toerso.healthconnect.exception.UserAlreadyExist;
import com.toerso.healthconnect.repository.AppointmentRepository;
import com.toerso.healthconnect.repository.DoctorRepository;
import com.toerso.healthconnect.repository.PatientRepository;
import com.toerso.healthconnect.repository.UserRepository;
import com.toerso.healthconnect.service.ReceptionistService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceptionistServiceImpl implements ReceptionistService {
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public PatientCreatedResponse createPatient(PatientCreateRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user != null)
            throw new UserAlreadyExist("User already exist with username: " + request.getUsername());

        String temporaryPassword = generateTemporaryPassword();

        user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(temporaryPassword))
                .roles(new HashSet<>(Set.of(RoleType.PATIENT)))
                .requiresPasswordChange(true)
                .build();
        user = userRepository.save(user);

        Patient patient = Patient.builder()
                .user(user)
                .name(request.getFullName())
                .age(request.getAge())
                .phone(request.getPhone())
                .build();
        patientRepository.save(patient);

        log.info("Patient created with ID: {}. Temp password generated.", user.getId());

        return new PatientCreatedResponse(user.getId(), temporaryPassword);
    }

    private String generateTemporaryPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 8; ++i) {
            builder.append(chars.charAt(random.nextInt(chars.length())));
        }

        return builder.toString();
    }

    @Override
    @Transactional
    public AppointmentCreatedResponse scheduleAppointment(AppointmentCreateRequest request) {
        //checking double booking
        if (appointmentRepository.existsByDoctorIdAndAppointmentTime(request.getDoctorId(), request.getAppointmentTime()))
            throw new AppointmentConflictException(request.getDoctorId(), request.getAppointmentTime());

        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() ->
                        new RuntimeException("Doctor not found")
                );
        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() ->
                        new RuntimeException("Patient not found")
                );

        Appointment appointment = Appointment.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentTime(request.getAppointmentTime())
                .appointmentStatus(AppointmentStatus.SCHEDULED)
                .build();

        try {
            appointment = appointmentRepository.save(appointment);
        }catch (OptimisticLockingFailureException e) {
            log.error("Conflict detected: Another receptionist updated this schedule.");
            throw new RuntimeException("Conflict: This slot was just taken. Please refresh.");
        }

        return AppointmentCreatedResponse.builder()
                .serialNo(appointment.getId())
                .doctorName(doctor.getName())
                .patientName(patient.getName())
                .appointmentTime(appointment.getAppointmentTime())
                .status(appointment.getAppointmentStatus().name())
                .build();
    }
}
