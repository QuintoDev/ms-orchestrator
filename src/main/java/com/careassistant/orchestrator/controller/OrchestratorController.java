package com.careassistant.orchestrator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careassistant.orchestrator.service.PacienteService;
import com.careassistant.orchestrator.service.ProfesionalService;

@RestController
@RequestMapping("/appointments")
public class OrchestratorController {

	private final ProfesionalService profesionalService;
	private final PacienteService pacienteService;

	public OrchestratorController(ProfesionalService profesionalService, PacienteService pacienteService) {
		this.profesionalService = profesionalService;
		this.pacienteService = pacienteService;
	}

	@GetMapping("/{uuid}/professional")
	public ResponseEntity<?> obtenerCitasPorProfesional(@PathVariable String uuid) {
		return ResponseEntity.ok(profesionalService.obtenerCitasProfesional(uuid));
	}
	
	@GetMapping("/{uuid}/patient")
	public ResponseEntity<?> obtenerCitasPorPaciente(@PathVariable String uuid) {
		return ResponseEntity.ok(pacienteService.obtenerCitasPaciente(uuid));
	}

	@PutMapping("/{id}/confirm")
	public ResponseEntity<?> confirmarCita(@PathVariable String id) {
		profesionalService.confirmarCita(id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<?> cancelarCita(@PathVariable String id) {
		profesionalService.cancelarCita(id);
		return ResponseEntity.ok().build();
	}

}
