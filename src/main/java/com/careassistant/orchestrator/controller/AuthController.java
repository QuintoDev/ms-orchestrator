package com.careassistant.orchestrator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careassistant.orchestrator.dto.UsuarioRequest;
import com.careassistant.orchestrator.dto.UsuarioResponse;
import com.careassistant.orchestrator.service.AuthOrchestratorService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthOrchestratorService authService;

    public AuthController(AuthOrchestratorService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@RequestBody UsuarioRequest request) {
        UsuarioResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
