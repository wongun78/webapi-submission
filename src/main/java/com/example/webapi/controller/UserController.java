package com.example.webapi.controller;

import com.example.webapi.domain.dto.user.UserResponse;
import com.example.webapi.repository.UserRepository;
import com.example.webapi.util.annotation.ApiMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    @ApiMessage("Get all users")
    public ResponseEntity<List<UserResponse>> getAll() {
        List<UserResponse> list = userRepository.findAll().stream()
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setUsername(user.getUsername());
                    response.setEmail(user.getEmail());
                    response.setRoles(user.getRoles().stream().map(r -> r.getName()).collect(java.util.stream.Collectors.toSet()));
                    return response;
                })
                .toList();
        return ResponseEntity.ok(list);
    }

}