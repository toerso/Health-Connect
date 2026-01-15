package com.toerso.healthconnect.service.implementation;

import com.toerso.healthconnect.dto.request.AppointmentCreateRequest;
import com.toerso.healthconnect.dto.response.AppointmentResponse;
import com.toerso.healthconnect.entity.Appointment;
import com.toerso.healthconnect.entity.Doctor;
import com.toerso.healthconnect.entity.Patient;
import com.toerso.healthconnect.enums.AppointmentStatus;
import com.toerso.healthconnect.exception.AppointmentConflictException;
import com.toerso.healthconnect.exception.ResourceNotFoundException;
import com.toerso.healthconnect.repository.AppointmentRepository;
import com.toerso.healthconnect.repository.DoctorRepository;
import com.toerso.healthconnect.repository.PatientRepository;
import com.toerso.healthconnect.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AppointmentResponse bookAppointment(AppointmentCreateRequest request) {
        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", request.getDoctorId()));
        Patient patient = patientRepository.findById(request.getPatientId()).orElseThrow(() -> new ResourceNotFoundException("Patient", "id", request.getPatientId()));

        //Prevent double booking
        boolean exists = appointmentRepository.existsByDoctorAndAppointmentTime(doctor, request.getAppointmentTime());

        if (exists) {
            throw new AppointmentConflictException(request.getDoctorId(), request.getAppointmentTime());
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentTime(request.getAppointmentTime());
        appointment.setAppointmentStatus(AppointmentStatus.SCHEDULED);

        Appointment saved = appointmentRepository.save(appointment);

        return modelMapper.map(saved, AppointmentResponse.class);
    }
}
