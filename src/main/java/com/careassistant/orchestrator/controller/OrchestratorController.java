package com.careassistant.orchestrator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careassistant.orchestrator.dto.ProfesionalResponse;
import com.careassistant.orchestrator.service.ProfesionalService;

@RestController
@RequestMapping("/searches")
public class OrchestratorController {

	private final ProfesionalService profesionalService;

	public OrchestratorController(ProfesionalService profesionalService) {
		this.profesionalService = profesionalService;
	}

	@GetMapping
	public List<ProfesionalResponse> buscar(@RequestParam String especialidad, @RequestParam String ciudad) {
		return profesionalService.buscarProfesionales(especialidad, ciudad);
	}

}
