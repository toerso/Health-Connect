package com.toerso.healthconnect.controller;

import com.toerso.healthconnect.security.AuthenticationService;
import com.toerso.healthconnect.dto.request.LoginRequest;
import com.toerso.healthconnect.dto.request.RegistrationRequest;
import com.toerso.healthconnect.dto.response.LoginResponse;
import com.toerso.healthconnect.dto.response.RegistrationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest request,
            @RequestParam UUID token) {
        return ResponseEntity.ok(authenticationService.registrationWithToken(request, token));
    }
}
