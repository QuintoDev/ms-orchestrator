package com.careassistant.orchestrator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.careassistant.orchestrator.dto.SolicitudCitaRequest;
import com.careassistant.orchestrator.service.PacienteService;
import com.careassistant.orchestrator.service.ProfesionalService;
import com.careassistant.orchestrator.service.SolicitudCitaService;

@RestController
@RequestMapping("/appointments")
public class OrchestratorController {

	private final ProfesionalService profesionalService;
	private final PacienteService pacienteService;
	private final SolicitudCitaService solicitudCitaService;

	public OrchestratorController(ProfesionalService profesionalService, PacienteService pacienteService,
			SolicitudCitaService solicitudCitaService) {
		this.profesionalService = profesionalService;
		this.pacienteService = pacienteService;
		this.solicitudCitaService = solicitudCitaService;
	}

	@GetMapping("/{uuid}/professional")
	public ResponseEntity<?> obtenerCitasPorProfesional(@PathVariable String uuid,
			@RequestHeader("Authorization") String authHeader) throws Exception {
		String token = authHeader.replace("Bearer ", "");
		return ResponseEntity.ok(profesionalService.obtenerCitasProfesional(uuid, token));
	}

	@GetMapping("/{uuid}/patient")
	public ResponseEntity<?> obtenerCitasPorPaciente(@PathVariable String uuid) throws Exception {
		return ResponseEntity.ok(pacienteService.obtenerCitasPaciente(uuid));
	}

	@PutMapping("/{id}/confirm")
	public ResponseEntity<?> confirmarCita(@PathVariable String id, @RequestHeader("Authorization") String authHeader)
			throws Exception {
		String token = authHeader.replace("Bearer ", "");
		profesionalService.confirmarCita(id, token);
		return ResponseEntity.ok().build();
	}

	@PutMapping("/{id}/cancel")
	public ResponseEntity<?> cancelarCita(@PathVariable String id, @RequestHeader("Authorization") String authHeader)
			throws Exception {
		String token = authHeader.replace("Bearer ", "");

		profesionalService.cancelarCita(id, token);
		return ResponseEntity.ok().build();
	}

	@PostMapping
	public ResponseEntity<Void> agendar(@RequestBody SolicitudCitaRequest request,
			@RequestHeader("Authorization") String authHeader) {
		String token = authHeader.replace("Bearer ", "");
		solicitudCitaService.solicitarServicio(request, token);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

}
