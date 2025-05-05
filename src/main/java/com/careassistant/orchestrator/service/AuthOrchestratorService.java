package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.LoginRequest;
import com.careassistant.orchestrator.dto.UsuarioResponse;

@Service
public class AuthOrchestratorService {

	private final RestTemplate restTemplate;

	@Value("${ms.users.url}")
	private String msUsersLoginUrl;

	public AuthOrchestratorService(RestTemplateBuilder builder) {
		this.restTemplate = builder.build();
	}

	public UsuarioResponse login(LoginRequest request) {
		String url = msUsersLoginUrl + "/login";
		ResponseEntity<UsuarioResponse> response = restTemplate.postForEntity(url, request, UsuarioResponse.class);
		return response.getBody();
	}
}
