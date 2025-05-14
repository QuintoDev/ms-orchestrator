package com.careassistant.orchestrator.service;

import com.careassistant.orchestrator.dto.SolicitudCitaRequest;
import com.careassistant.orchestrator.security.JWTUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class SolicitudCitaService {

	private final JWTUtility jwtUtility;
	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${ms.services.url}")
	private String msServicesUrl;

	public SolicitudCitaService(JWTUtility jwtUtility) {
		this.jwtUtility = jwtUtility;
	}

	public void solicitarServicio(SolicitudCitaRequest request, String token) {
		UUID uuidPaciente = jwtUtility.obtenerUserId(token);

		Map<String, Object> body = new HashMap<>();
		body.put("uuidPaciente", uuidPaciente.toString());
		body.put("uuidProfesionalSalud", request.getUuidProfesionalSalud());
		body.put("especialidad", request.getEspecialidad());
		body.put("fecha", request.getFecha());
		body.put("hora", request.getHora());
		body.put("resumen", request.getResumen());
		body.put("ubicacion", request.getUbicacion());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

		restTemplate.postForEntity(msServicesUrl + "/appointments", entity, Void.class);
	}
}
