package com.toerso.healthconnect.service.implementation;

import com.toerso.healthconnect.dto.request.PatientCreateRequest;
import com.toerso.healthconnect.dto.response.PatientResponse;
import com.toerso.healthconnect.entity.Patient;
import com.toerso.healthconnect.repository.PatientRepository;
import com.toerso.healthconnect.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final ModelMapper modelMapper;

    @Override
    public PatientResponse createPatient(PatientCreateRequest request) {
        Patient patient = modelMapper.map(request, Patient.class);
        Patient saved= patientRepository.save(patient);

        return modelMapper.map(saved, PatientResponse.class);
    }
}
