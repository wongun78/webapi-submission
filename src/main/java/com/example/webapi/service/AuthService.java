package com.example.webapi.service;

import com.example.webapi.domain.dto.auth.AuthResponse;
import com.example.webapi.domain.dto.auth.LoginRequest;
import com.example.webapi.domain.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    
    void register(RegisterRequest request);
}