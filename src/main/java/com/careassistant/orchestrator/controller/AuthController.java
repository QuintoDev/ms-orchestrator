package com.careassistant.orchestrator.controller;

import com.careassistant.orchestrator.dto.LoginRequest;
import com.careassistant.orchestrator.dto.LoginResponse;
import com.careassistant.orchestrator.service.AuthOrchestratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthOrchestratorService authService;

    public AuthController(AuthOrchestratorService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
