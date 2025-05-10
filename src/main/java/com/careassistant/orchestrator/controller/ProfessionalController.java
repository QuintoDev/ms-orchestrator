package com.careassistant.orchestrator.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careassistant.orchestrator.dto.ProfesionalResponse;
import com.careassistant.orchestrator.service.ProfesionalService;

@RestController
public class ProfessionalController {

	private final ProfesionalService profesionalService;

	public ProfessionalController(ProfesionalService profesionalService) {
		this.profesionalService = profesionalService;
	}

	@GetMapping("/searches")
	public List<ProfesionalResponse> buscar(@RequestParam String especialidad, @RequestParam String ciudad) {
		return profesionalService.buscarProfesionales(especialidad, ciudad);
	}

	@GetMapping("/appointments/{uuid}/professional")
	public ResponseEntity<?> obtenerCitasPorProfesional(@PathVariable String uuid) {
		return ResponseEntity.ok(profesionalService.obtenerCitasProfesional(uuid));
	}

	@PutMapping("/appointments/{id}/confirm")
	public ResponseEntity<?> confirmarCita(@PathVariable String id) {
		profesionalService.confirmarCita(id);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/appointments/{id}/cancel")
	public ResponseEntity<?> cancelarCita(@PathVariable String id) {
		profesionalService.cancelarCita(id);
		return ResponseEntity.ok().build();
	}

}
