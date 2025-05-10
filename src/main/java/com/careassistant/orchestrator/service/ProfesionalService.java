package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.ProfesionalResponse;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfesionalService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${ms.users.url}")
	private String msUsersUrl;

	@Value("${ms.services.url}")
	private String msServicesUrl;

	public List<ProfesionalResponse> buscarProfesionales(String especialidad, String ciudad) {
		String url = msUsersUrl + "/users?especialidad=" + especialidad + "&ciudad=" + ciudad;
		ProfesionalResponse[] response = restTemplate.getForObject(url, ProfesionalResponse[].class);
		return Arrays.asList(response);
	}

	public List<?> obtenerCitasProfesional(String uuidProfesional) {
		String url = msServicesUrl + "/appointments/" + uuidProfesional + "/professional";
		ResponseEntity<?> response = restTemplate.getForEntity(url, Object.class);
		return (List<?>) response.getBody();
	}

	public void confirmarCita(String id) {
		String url = msServicesUrl + "/appointments/" + id + "/confirm";
		restTemplate.put(url, null);
	}

	public void cancelarCita(String id) {
		String url = msServicesUrl + "/appointments/" + id + "/cancel";
		restTemplate.put(url, null);
	}
}
