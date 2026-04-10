package com.toerso.healthconnect.service.implementation;

import com.toerso.healthconnect.contract.MedicalRecordProjection;
import com.toerso.healthconnect.dto.response.AppointmentResponse;
import com.toerso.healthconnect.dto.response.MedicalRecordResponse;
import com.toerso.healthconnect.dto.response.PatientSearchedResponse;
import com.toerso.healthconnect.dto.response.PrescriptionResponse;
import com.toerso.healthconnect.entity.Appointment;
import com.toerso.healthconnect.entity.Patient;
import com.toerso.healthconnect.entity.User;
import com.toerso.healthconnect.exception.EntityNotFoundException;
import com.toerso.healthconnect.repository.AppointmentRepository;
import com.toerso.healthconnect.repository.DoctorRepository;
import com.toerso.healthconnect.repository.MedicalRecordRepository;
import com.toerso.healthconnect.repository.PatientRepository;
import com.toerso.healthconnect.service.DoctorService;
import com.toerso.healthconnect.specification.PatientSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordRepository medicalRecordRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> getDoctorSchedule(User doctor, Pageable pageable) {
        log.debug("Fetching scheduled appointment for doctor id {}", doctor.getId());

        return appointmentRepository.findByDoctorId(doctor.getId(), pageable).map(appointment ->
                AppointmentResponse.builder()
                        .id(appointment.getId())
                        .patientName(appointment.getPatient().getName())
                        .doctorName(appointment.getDoctor().getName())
                        .appointmentTime(appointment.getAppointmentTime())
                        .appointmentStatus(appointment.getAppointmentStatus().toString())
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PatientSearchedResponse> searchPatients(User doctor, String name, String phone, Pageable pageable) {
        Specification<Patient> specification = Specification.where(PatientSpecifications.hasNameLike(name))
                .and(PatientSpecifications.hasPhone(phone));

        return patientRepository.findAll(specification, pageable).map(patient ->
                PatientSearchedResponse.builder()
                        .id(patient.getId())
                        .name(patient.getName())
                        .age(patient.getAge())
                        .phone(patient.getPhone())
                        .build()
                );
    }

    @Override
    @Transactional(readOnly = true)
    public Page<MedicalRecordResponse> getPatientMedicalHistory(User doctor, Long patientId, Pageable pageable) {
        Page<MedicalRecordProjection> projections = medicalRecordRepository.findByPatientId(patientId, pageable);

        return projections.map(medicalRecordProjection ->
                MedicalRecordResponse.builder()
                        .id(medicalRecordProjection.getId())
                        .patientName(medicalRecordProjection.getPatientName())
                        .doctorName(medicalRecordProjection.getDoctorName())
                        .symptoms(medicalRecordProjection.getSymptoms())
                        .diagnosis(medicalRecordProjection.getDiagnosis())
                        .lastVisitDate(medicalRecordProjection.getVisitDate())
                        .prescriptionList(medicalRecordProjection.getPrescriptions().stream().map(prescription ->
                                        PrescriptionResponse.builder()
                                                .id(prescription.getId())
                                                .medicine(prescription.getMedicine())
                                                .dosage(prescription.getDosage())
                                                .efficacy(prescription.getEfficacy())
                                                .duration(prescription.getDuration())
                                                .build()
                                ).toList())
                        .build()
                );
    }


}
