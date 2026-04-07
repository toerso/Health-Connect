package com.toerso.healthconnect.controller;

import com.toerso.healthconnect.dto.request.AppointmentCreateRequest;
import com.toerso.healthconnect.dto.request.PatientCreateRequest;
import com.toerso.healthconnect.dto.response.AppointmentCreatedResponse;
import com.toerso.healthconnect.dto.response.PatientCreatedResponse;
import com.toerso.healthconnect.service.ReceptionistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reception")
@RequiredArgsConstructor
public class ReceptionistController {
    private final ReceptionistService receptionistService;

    @PostMapping("/admit-patient")
    public ResponseEntity<PatientCreatedResponse> createPatient(@Valid @RequestBody PatientCreateRequest request) {
        return ResponseEntity.ok(receptionistService.createPatient(request));
    }

    @PostMapping("/book-appointment")
    public ResponseEntity<AppointmentCreatedResponse> scheduleAppointment(@Valid @RequestBody AppointmentCreateRequest request) {
        return ResponseEntity.ok(receptionistService.scheduleAppointment(request));
    }
}
