package com.toerso.healthconnect.service.implementation;

import com.toerso.healthconnect.annotation.audit.Audited;
import com.toerso.healthconnect.contract.MedicalRecordProjection;
import com.toerso.healthconnect.dto.request.EncounterRequest;
import com.toerso.healthconnect.dto.response.AppointmentResponse;
import com.toerso.healthconnect.dto.response.MedicalRecordResponse;
import com.toerso.healthconnect.dto.response.PatientSearchedResponse;
import com.toerso.healthconnect.dto.response.PrescriptionResponse;
import com.toerso.healthconnect.entity.*;
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

import java.time.LocalDateTime;
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
    @Transactional
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

    @Audited(
            action = "SEARCH_PATIENT",
            targetType = "Patient"
    )
    @Override
    @Transactional
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

    @Audited(
            action = "ACCESS_MEDICAL_HISTORY",
            targetType = "Patient",
            targetIdArgIndex = 1
    )
    @Override
    @Transactional
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
    @Audited(
            action = "CREATE_MEDICAL_RECORD",
            targetType = "Patient",
            targetIdArgIndex = 1
    )
    @Transactional
    public void createEncounter(User user, Long patientId, EncounterRequest request) {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() ->
                new EntityNotFoundException("Patient doesn't exists with id: " + patientId)
        );

        Doctor doctor = doctorRepository.findById(user.getId()).orElse(null);

        MedicalRecord record = new MedicalRecord();
        record.setPatient(patient);
        record.setDoctor(doctor);
        record.setSymptoms(request.symptoms());
        record.setDiagnosis(request.diagnosis());
        record.setVisitDate(LocalDateTime.now());

        List<Prescription> prescriptions = request.prescriptions().stream()
                .map(dto ->
                        Prescription.builder()
                                .medicine(dto.medicine())
                                .efficacy(dto.efficacy())
                                .dosage(dto.dosage())
                                .duration(dto.duration())
                                .instructions(dto.instructions())
                                .medicalRecord(record)
                                .build()
                        )
                .toList();

        record.setPrescriptions(prescriptions);

        medicalRecordRepository.save(record);
        log.info("Medical record created for patient {} by doctor {}", patientId, user.getId());
    }
}
