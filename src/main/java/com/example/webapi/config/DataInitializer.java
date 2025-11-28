package com.example.webapi.config;

import com.example.webapi.domain.entity.Role;
import com.example.webapi.domain.entity.User;
import com.example.webapi.repository.RoleRepository;
import com.example.webapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void initializeData() {
        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_ADMIN");
                    role.setDescription("Administrator role");
                    return roleRepository.save(role);
                });

        Role reporterRole = roleRepository.findByName("ROLE_REPORTER")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setName("ROLE_REPORTER");
                    role.setDescription("Reporter role");
                    return roleRepository.save(role);
                });

        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("password123"));
            admin.setStatus(com.example.webapi.util.constant.UserStatusEnum.ACTIVE);
            
            Set<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);
            
            userRepository.save(admin);
        }

        if (!userRepository.existsByUsername("reporter")) {
            User reporter = new User();
            reporter.setUsername("reporter");
            reporter.setEmail("reporter@example.com");
            reporter.setPassword(passwordEncoder.encode("password123"));
            reporter.setStatus(com.example.webapi.util.constant.UserStatusEnum.ACTIVE);
            
            Set<Role> reporterRoles = new HashSet<>();
            reporterRoles.add(reporterRole);
            reporter.setRoles(reporterRoles);
            
            userRepository.save(reporter);
        }
    }
}
