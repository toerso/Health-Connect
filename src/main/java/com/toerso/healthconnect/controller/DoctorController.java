package com.toerso.healthconnect.controller;

import com.toerso.healthconnect.dto.request.EncounterRequest;
import com.toerso.healthconnect.dto.response.AppointmentResponse;
import com.toerso.healthconnect.dto.response.MedicalRecordResponse;
import com.toerso.healthconnect.dto.response.PatientSearchedResponse;
import com.toerso.healthconnect.entity.User;
import com.toerso.healthconnect.service.AuditLogService;
import com.toerso.healthconnect.service.DoctorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/doctor")
public class DoctorController {
    private final DoctorService doctorService;
    private final AuditLogService auditLogService;

    /**
     * Requirement: See appointments he has (Paginated)
     * URL Example: /api/v1/doctor/appointments?page=0&size=10
     */
    @GetMapping("/appointments")
    public ResponseEntity<Page<AppointmentResponse>> getMySchedule(
            @AuthenticationPrincipal User user,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Doctor {} fetching paginated schedule", user.getUsername());

        return ResponseEntity.ok(doctorService.getDoctorSchedule(user, pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PatientSearchedResponse>> searchPatients(
            @AuthenticationPrincipal User user,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            Pageable pageable
    ) {
        log.info("Doctor {} searching patients with name {} and phone {}", user.getUsername(), name, phone);

        return ResponseEntity.ok(doctorService.searchPatients(user, name, phone, pageable));
    }

    @GetMapping("/patient/{id}/history")
    public ResponseEntity<Page<MedicalRecordResponse>> getPatientMedicalHistory(
            @AuthenticationPrincipal User user,
            @PathVariable Long id,
            Pageable pageable
    ) {
        return ResponseEntity.ok(doctorService.getPatientMedicalHistory(user, id, pageable));
    }

    @PostMapping("/patient/{id}/encounter")
    public ResponseEntity<String> createEncounter(
            @AuthenticationPrincipal User user,
            @RequestBody EncounterRequest request,
            @RequestParam Long id
    ) {
        doctorService.createEncounter(user, id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Medical record is created successfully for patient: " + id);
    }
}
