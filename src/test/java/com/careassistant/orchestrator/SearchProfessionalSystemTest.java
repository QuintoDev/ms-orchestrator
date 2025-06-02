package com.careassistant.orchestrator;

import com.careassistant.orchestrator.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchProfessionalSystemTest {

	@LocalServerPort
	private int port;

	private final RestTemplate restTemplate = new RestTemplate();

	@Test
	public void buscarProfesionalTest() {
		// 1. Crear paciente y profesional
		Map<String, Object> paciente = TestUtils.crearPaciente(restTemplate, port);
		Map<String, Object> profesional = TestUtils.crearProfesional(restTemplate, port);

		// 2. Hacer login del paciente para obtener token
		String token = TestUtils.loginPaciente(restTemplate, port, paciente);

		// 3. Realizar búsqueda
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);

		HttpEntity<Void> entity = new HttpEntity<>(headers);
		String url = "http://localhost:" + port + "/searches?especialidad=geriatria&ciudad=Bogotá";

		ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
				new ParameterizedTypeReference<>() {
				});

		assertEquals(HttpStatus.OK, response.getStatusCode());
		List<Map<String, Object>> resultados = response.getBody();
		assertNotNull(resultados);
		assertFalse(resultados.isEmpty());

		// 4. Eliminar usuarios creados
		TestUtils.eliminarUsuario(restTemplate, paciente.get("id").toString());
		TestUtils.eliminarUsuario(restTemplate, profesional.get("id").toString());
	}
}