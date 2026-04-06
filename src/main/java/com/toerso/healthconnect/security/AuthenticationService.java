package com.toerso.healthconnect.security;

import com.toerso.healthconnect.dto.request.InvitationRequest;
import com.toerso.healthconnect.dto.request.LoginRequest;
import com.toerso.healthconnect.dto.request.RegistrationRequest;
import com.toerso.healthconnect.dto.response.InvitationResponse;
import com.toerso.healthconnect.dto.response.LoginResponse;
import com.toerso.healthconnect.dto.response.RegistrationResponse;
import com.toerso.healthconnect.entity.*;
import com.toerso.healthconnect.enums.RoleType;
import com.toerso.healthconnect.exception.UserAlreadyExist;
import com.toerso.healthconnect.repository.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final InvitationRepository invitationRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final ReceptionistRepository receptionistRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtUtil.generateToken(user);

        return new LoginResponse(token, user.getId());
    }


    @Transactional
    private User registrationInternal(RegistrationRequest request, UUID token) {
        Invitation invite = invitationRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired invitation"));

        if (invite.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Invitation has expired");
        }

        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user != null) throw new UserAlreadyExist("User already exist with id : " + user.getId());

        user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(new HashSet<>(invite.getAssignedRoles()))
                .build();
        userRepository.save(user);

        if (invite.getAssignedRoles().contains(RoleType.DOCTOR)) {
            createDoctorProfile(user, request);
        } else if (invite.getAssignedRoles().contains(RoleType.PATIENT)) {
            createPatientProfile(user, request);
        }else if (invite.getAssignedRoles().contains(RoleType.RECEPTIONIST)) {
            createReceptionistProfile(user, request);
        }

        invite.setUsed(false);
        invitationRepository.save(invite);

        return user;
    }

    private void createDoctorProfile(User user, RegistrationRequest request) {
        Doctor doctor = Doctor.builder()
                .user(user)
                .name(request.getName())
                .age(request.getAge())
                .phone(request.getPhone())
                .specialization(request.getSpecialization())
                .build();

        doctorRepository.save(doctor);
    }

    private void createPatientProfile(User user, RegistrationRequest request) {
        Patient patient = Patient.builder()
                .user(user)
                .name(request.getName())
                .age(request.getAge())
                .phone(request.getPhone())
                .build();

        patientRepository.save(patient);
    }

    private void createReceptionistProfile(User user, RegistrationRequest request) {
        Receptionist receptionist = Receptionist.builder()
                .user(user)
                .name(request.getName())
                .age(request.getAge())
                .phone(request.getPhone())
                .build();

        receptionistRepository.save(receptionist);
    }

    public RegistrationResponse registrationWithToken(RegistrationRequest request, UUID token) {
        User user = registrationInternal(request, token);

        return RegistrationResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .message("Registration successful")
                .build();
    }

    public InvitationResponse createInvitation(@Valid InvitationRequest request) {
        Invitation invite = Invitation.builder()
                .email(request.getEmail())
                .assignedRoles(request.getRoles())
                .expiryDate(LocalDateTime.now().plusDays(7))
                .build();

        invite = invitationRepository.save(invite);

        String registerUrl = "localhost:8080/api/v1/auth/register?token=" + invite.getToken();

        return new InvitationResponse(registerUrl);
    }
}
