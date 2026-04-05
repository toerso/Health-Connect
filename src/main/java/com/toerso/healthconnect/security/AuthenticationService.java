package com.toerso.healthconnect.security;

import com.toerso.healthconnect.dto.request.LoginRequest;
import com.toerso.healthconnect.dto.request.RegistrationRequest;
import com.toerso.healthconnect.dto.response.LoginResponse;
import com.toerso.healthconnect.dto.response.RegistrationResponse;
import com.toerso.healthconnect.entity.User;
import com.toerso.healthconnect.enums.RoleType;
import com.toerso.healthconnect.exception.UserAlreadyExist;
import com.toerso.healthconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
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

    private User registerInternal(RegistrationRequest request) {
        User user = userRepository.findByUsername(request.getUsername()).orElse(null);

        if (user != null) throw new UserAlreadyExist("User already exist with id : " + user.getId());

        user = modelMapper.map(request, User.class);

        user.setRoles(new HashSet<>(Set.of(RoleType.PATIENT)));
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public RegistrationResponse register(RegistrationRequest request) {
        User user = registerInternal(request);

        return modelMapper.map(user, RegistrationResponse.class);
    }
}
