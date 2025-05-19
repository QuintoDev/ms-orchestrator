package com.careassistant.orchestrator.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.careassistant.orchestrator.service.ProfesionalService;
import com.careassistant.orchestrator.dto.BuscarProfesionalResponse;

@RestController
@RequestMapping("/searches")
public class ProfesionalController {

	private final ProfesionalService profesionalService;

	public ProfesionalController(ProfesionalService profesionalService) {
		this.profesionalService = profesionalService;
	}

	@GetMapping
	public List<BuscarProfesionalResponse> buscar(@RequestParam String especialidad, @RequestParam String ciudad)
			throws Exception {
		return profesionalService.buscarProfesionales(especialidad, ciudad);
	}

}
