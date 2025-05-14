package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.UsuarioRequest;
import com.careassistant.orchestrator.dto.UsuarioResponse;

@Service
public class UsuarioService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${ms.users.url}")
	private String msUsersUrl;

	public ResponseEntity<?> registrarUsuario(UsuarioRequest usuario) {
		String url = msUsersUrl + "/users";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<UsuarioRequest> entity = new HttpEntity<>(usuario, headers);

		try {
			ResponseEntity<UsuarioResponse> response = restTemplate.postForEntity(url, entity, UsuarioResponse.class);
			return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		}
	}
}
