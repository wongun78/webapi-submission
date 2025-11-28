package com.example.webapi.controller;

import com.example.webapi.domain.dto.auth.AuthResponse;
import com.example.webapi.domain.dto.auth.LoginRequest;
import com.example.webapi.domain.dto.auth.RegisterRequest;
import com.example.webapi.service.AuthService;
import com.example.webapi.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @ApiMessage("Login successfully")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    @ApiMessage("Register successfully")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok(null);
    }
}