package com.toerso.healthconnect.unit.service;

import com.toerso.healthconnect.dto.response.AppointmentResponse;
import com.toerso.healthconnect.entity.Appointment;
import com.toerso.healthconnect.entity.Doctor;
import com.toerso.healthconnect.entity.Patient;
import com.toerso.healthconnect.entity.User;
import com.toerso.healthconnect.enums.AppointmentStatus;
import com.toerso.healthconnect.enums.RoleType;
import com.toerso.healthconnect.repository.AppointmentRepository;
import com.toerso.healthconnect.repository.DoctorRepository;
import com.toerso.healthconnect.repository.MedicalRecordRepository;
import com.toerso.healthconnect.repository.PatientRepository;
import com.toerso.healthconnect.service.implementation.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TestDoctorService {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private Patient patient;
    private Appointment appointment;
    private User user;


    @BeforeEach
    void setup() {
        doctor = Doctor.builder()
                .id(1L)
                .name("Sakurah Saku")
                .age(26)
                .phone("01922234554")
                .email("saku@mail.com")
                .specialization("Neurologist")
                .build();

        user = User.builder()
                .id(1L)
                .username("saku")
                .password("saku@12345")
                .roles(new HashSet<>(List.of(RoleType.DOCTOR)))
                .doctor(doctor)
                .build();

        patient = Patient.builder()
                .id(2L)
                .name("Abdul Kader")
                .age(56)
                .phone("01345555555")
                .build();

        appointment = Appointment.builder()
                .id(10L)
                .patient(patient)
                .doctor(doctor)
                .appointmentStatus(AppointmentStatus.SCHEDULED)
                .appointmentTime(LocalDateTime.now())
                .build();

    }


    @Test
    void shouldReturnDoctorScheduleSuccessfully() {
        //Arrange
        Pageable pageable = PageRequest.of(0, 10);

        Page<Appointment> appointmentPage = new PageImpl<>(List.of(appointment));

        when(appointmentRepository.findByDoctorId(user.getId(), pageable))
                .thenReturn(appointmentPage);

        //Act
        Page<AppointmentResponse> result = doctorService.getDoctorSchedule(user, pageable);

        //Asserting
        assertThat(result).hasSize(1);
        AppointmentResponse appointmentResponse = result.getContent().get(0);

        assertThat(appointmentResponse.getId()).isEqualTo(10L);
        
    }
}