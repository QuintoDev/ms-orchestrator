package com.careassistant.orchestrator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careassistant.orchestrator.service.UsuarioService;

@RestController
@RequestMapping("/users")
public class UsuarioController {

	private final UsuarioService usuarioService;

	public UsuarioController(UsuarioService usuarioService) {
		super();
		this.usuarioService = usuarioService;
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> buscarPorId(@PathVariable String id, @RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replace("Bearer ", "");
		return usuarioService.obtenerUsuarioPorId(id, token);
	}

}
