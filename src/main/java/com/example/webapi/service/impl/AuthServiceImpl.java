package com.example.webapi.service.impl;

import com.example.webapi.domain.dto.auth.AuthResponse;
import com.example.webapi.domain.dto.auth.LoginRequest;
import com.example.webapi.domain.dto.auth.RegisterRequest;
import com.example.webapi.domain.entity.Role;
import com.example.webapi.domain.entity.User;
import com.example.webapi.exception.BadRequestException;
import com.example.webapi.repository.RoleRepository;
import com.example.webapi.repository.UserRepository;
import com.example.webapi.service.AuthService;
import com.example.webapi.util.SecurityUtil;
import com.example.webapi.util.constant.AppConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final SecurityUtil securityUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadRequestException("User not found"));

        AuthResponse res = new AuthResponse();
        AuthResponse.UserLogin userLogin = new AuthResponse.UserLogin();
        userLogin.setId(user.getId());
        userLogin.setEmail(user.getEmail());
        userLogin.setUsername(user.getUsername());
        userLogin.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        res.setUser(userLogin);

        String accessToken = securityUtil.createAccessToken(authentication.getName(), res);
        res.setAccessToken(accessToken);

        return res;
    }

    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username already exists");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        Role userRole = roleRepository.findByName(AppConstants.ROLE_USER)
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName(AppConstants.ROLE_USER);
                    return roleRepository.save(newRole);
                });

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setStatus(com.example.webapi.util.constant.UserStatusEnum.ACTIVE);
        user.setRoles(Collections.singleton(userRole));

        userRepository.save(user);
    }
}