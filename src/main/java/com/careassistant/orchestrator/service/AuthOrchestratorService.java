package com.careassistant.orchestrator.service;

import com.careassistant.orchestrator.dto.LoginRequest;
import com.careassistant.orchestrator.dto.LoginResponse;
import com.careassistant.orchestrator.security.JWTUtility;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class AuthOrchestratorService {

	@Value("${ms.users.url}")
	private String msUsersUrl;

	private final JWTUtility jwtUtility;
	private final RestTemplate restTemplate = new RestTemplate();

	public AuthOrchestratorService(JWTUtility jwtUtility) {
		this.jwtUtility = jwtUtility;
	}

	public LoginResponse login(LoginRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(msUsersUrl + "/users/login",
				HttpMethod.POST, httpEntity, new ParameterizedTypeReference<Map<String, Object>>() {
				});

		if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
			throw new RuntimeException("Credenciales inválidas");
		}

		Map<String, Object> body = response.getBody();
		String correo = (String) body.get("correo");
		String tipo = (String) body.get("tipo");

		String token = jwtUtility.generarToken(correo, tipo);
		return new LoginResponse(token, correo, tipo);

	}
}
