package com.careassistant.orchestrator.service;

import java.util.UUID;

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
import com.careassistant.orchestrator.security.JWTUtility;

@Service
public class UsuarioService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final JWTUtility jwtUtility;

	public UsuarioService(JWTUtility jwtUtility) {
		super();
		this.jwtUtility = jwtUtility;
	}

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

	public ResponseEntity<?> obtenerUsuarioPorId(String id, String token) {
		UUID uuidDelToken = jwtUtility.obtenerUserId(token);
		String url = msUsersUrl + "/users/" + id;

		if (!id.equals(uuidDelToken.toString())) {
			throw new SecurityException("Acceso no autorizado");
		}

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		try {
			ResponseEntity<UsuarioResponse> response = restTemplate.getForEntity(url, UsuarioResponse.class);
			return ResponseEntity.status(response.getStatusCode()).body(response.getBody());

		} catch (HttpClientErrorException e) {
			return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
		}

	}
}
