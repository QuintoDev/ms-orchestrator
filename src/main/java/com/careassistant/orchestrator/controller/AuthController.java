package com.careassistant.orchestrator.controller;

import com.careassistant.orchestrator.dto.LoginRequest;
import com.careassistant.orchestrator.dto.LoginResponse;
import com.careassistant.orchestrator.dto.UsuarioRequest;
import com.careassistant.orchestrator.service.AuthOrchestratorService;
import com.careassistant.orchestrator.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

	private final AuthOrchestratorService authService;
	private final UsuarioService usuarioservice;

	public AuthController(AuthOrchestratorService authService, UsuarioService usuarioservice) {
		super();
		this.authService = authService;
		this.usuarioservice = usuarioservice;
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request));
	}

	@PostMapping("/signup")
	public ResponseEntity<?> createUser(@RequestBody UsuarioRequest usuario) {
		return ResponseEntity.ok(usuarioservice.registrarUsuario(usuario));
	}
}
