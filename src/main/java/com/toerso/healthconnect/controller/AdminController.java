package com.toerso.healthconnect.controller;

import com.toerso.healthconnect.dto.request.InvitationRequest;
import com.toerso.healthconnect.dto.response.InvitationResponse;
import com.toerso.healthconnect.security.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AuthenticationService authenticationService;

    @PostMapping("/invite")
    public ResponseEntity<InvitationResponse> inviteUser(@Valid @RequestBody InvitationRequest request) {
        return ResponseEntity.ok(authenticationService.createInvitation(request));
    }
}
