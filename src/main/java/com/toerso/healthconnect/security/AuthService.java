package com.toerso.healthconnect.security;

import com.toerso.healthconnect.dto.request.LoginRequest;
import com.toerso.healthconnect.dto.response.LoginResponse;
import com.toerso.healthconnect.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = (User) authentication.getPrincipal();
        assert user != null;
        String token = JwtUtil.generateToken(user);

        return new LoginResponse(token, user.getId());
    }
}
