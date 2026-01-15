package com.toerso.healthconnect;

import com.toerso.healthconnect.dto.request.PatientCreateRequest;
import com.toerso.healthconnect.dto.response.PatientResponse;
import com.toerso.healthconnect.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class PatientTests {

    @Autowired
    public PatientService patientService;

    @Test
    public void testPatientCreation() {
        PatientCreateRequest request = new PatientCreateRequest();
        request.setName("John Doe");
        request.setPhone("01322529083");
        request.setDateOfBirth(LocalDate.of(2000, 7, 4));
        request.setMedicalHistory("Fever, Diarrhoea");

        PatientResponse patientResponse = patientService.createPatient(request);

        System.out.println(patientResponse);
    }
}
