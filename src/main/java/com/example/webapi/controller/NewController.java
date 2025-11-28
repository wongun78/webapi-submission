package com.example.webapi.controller;

import com.example.webapi.domain.dto.common.DashboardResponse;
import com.example.webapi.domain.dto.news.NewRequest;
import com.example.webapi.domain.dto.news.NewResponse;
import com.example.webapi.service.NewService;
import com.example.webapi.util.annotation.ApiMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewController {

    private final NewService newService;

    @GetMapping
    @ApiMessage("Get all news")
    public ResponseEntity<List<NewResponse>> getAll() {
        return ResponseEntity.ok(newService.getAll());
    }

    @GetMapping("/{id}")
    @ApiMessage("Get new by id")
    public ResponseEntity<NewResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(newService.getById(id));
    }

    @PreAuthorize("hasRole('ROLE_REPORTER')")
    @PostMapping
    @ApiMessage("Create new")
    public ResponseEntity<NewResponse> create(
            @Valid @RequestBody NewRequest request) {
        return ResponseEntity.ok(newService.create(request));
    }

    @PreAuthorize("hasRole('ROLE_REPORTER')")
    @PutMapping("/{id}")
    @ApiMessage("Update new")
    public ResponseEntity<NewResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody NewRequest request) {
        return ResponseEntity.ok(newService.update(id, request));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_REPORTER')")
    @DeleteMapping("/{id}")
    @ApiMessage("Deleted new")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        newService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/{id}/publish")
    @ApiMessage("Publish news")
    public ResponseEntity<NewResponse> publishNews(@PathVariable Long id) {
        return ResponseEntity.ok(newService.publishNews(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/dashboard")
    @ApiMessage("Get dashboard statistics")
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(newService.getDashboard());
    }
}