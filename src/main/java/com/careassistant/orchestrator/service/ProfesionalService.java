package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.ProfesionalResponse;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfesionalService {

	private final RestTemplate restTemplate = new RestTemplate();
	
	@Value("${ms.users.url}")
	private String msUsersLoginUrl;

	public List<ProfesionalResponse> buscarProfesionales(String especialidad, String ciudad) {
		String url = msUsersLoginUrl + "/users?especialidad=" + especialidad + "&ciudad="
				+ ciudad;
		ProfesionalResponse[] response = restTemplate.getForObject(url, ProfesionalResponse[].class);
		return Arrays.asList(response);
	}
}
