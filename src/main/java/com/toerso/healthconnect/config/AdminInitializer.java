package com.toerso.healthconnect.config;

import com.toerso.healthconnect.entity.User;
import com.toerso.healthconnect.enums.RoleType;
import com.toerso.healthconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByRolesContaining(RoleType.ADMIN)) {
            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("Admin@1234"))
                    .roles(Set.of(RoleType.ADMIN))
                    .build();

            userRepository.save(admin);
            log.info("Initial admin created successfully");
        }
    }
}
