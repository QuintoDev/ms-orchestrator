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
public class GetAppointmentsSystemTest {

	@LocalServerPort
	private int port;

	private final RestTemplate restTemplate = new RestTemplate();

	@Test
	public void verCitasAsignadasTest() {
		// 1. Crear paciente y profesional
		Map<String, Object> paciente = TestUtils.crearPaciente(restTemplate, port);
		Map<String, Object> profesional = TestUtils.crearProfesional(restTemplate, port);

		// 2. Hacer login y obtener tokens
		String tokenPaciente = TestUtils.loginPaciente(restTemplate, port, paciente);
		String tokenProfesional = TestUtils.loginProfesional(restTemplate, port, profesional);

		// 3. Asignar una cita como paciente
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(tokenPaciente);
		headers.setContentType(MediaType.APPLICATION_JSON);

		String urlAsignar = "http://localhost:" + port + "/appointments";

		Map<String, Object> body = Map.of("fecha", "2025-06-02", "hora", "15:30", "uuidProfesionalSalud",
				profesional.get("id"), "resumen", "Prueba funcional de sistema", "ubicacion", "Casa");

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
		ResponseEntity<Void> responseAsignacion = restTemplate.exchange(urlAsignar, HttpMethod.POST, entity,
				Void.class);

		assertEquals(HttpStatus.CREATED, responseAsignacion.getStatusCode());

		// 4. Obtener citas del paciente
		String uuidPaciente = TestUtils.extraerUUIDDesdeToken(tokenPaciente);
		String urlCitasPaciente = "http://localhost:" + port + "/appointments/" + uuidPaciente + "/patient";

		HttpHeaders headersPaciente = new HttpHeaders();
		headersPaciente.setBearerAuth(tokenPaciente);
		HttpEntity<Void> entityPaciente = new HttpEntity<>(headersPaciente);

		ResponseEntity<List<Map<String, Object>>> respuestaPaciente = restTemplate.exchange(urlCitasPaciente,
				HttpMethod.GET, entityPaciente, new ParameterizedTypeReference<>() {
				});

		assertEquals(HttpStatus.OK, respuestaPaciente.getStatusCode());
		assertNotNull(respuestaPaciente.getBody());
		assertFalse(respuestaPaciente.getBody().isEmpty());

		// 5. Obtener citas del profesional
		String uuidProfesional = TestUtils.extraerUUIDDesdeToken(tokenProfesional);
		String urlCitasProfesional = "http://localhost:" + port + "/appointments/" + uuidProfesional + "/professional";

		HttpHeaders headersProfesional = new HttpHeaders();
		headersProfesional.setBearerAuth(tokenProfesional);
		HttpEntity<Void> entityProfesional = new HttpEntity<>(headersProfesional);

		ResponseEntity<List<Map<String, Object>>> respuestaProfesional = restTemplate.exchange(urlCitasProfesional,
				HttpMethod.GET, entityProfesional, new ParameterizedTypeReference<>() {
				});

		assertEquals(HttpStatus.OK, respuestaProfesional.getStatusCode());
		assertNotNull(respuestaProfesional.getBody());
		assertFalse(respuestaProfesional.getBody().isEmpty());

		// 6. Eliminar usuarios
		TestUtils.eliminarUsuario(restTemplate, paciente.get("id").toString());
		TestUtils.eliminarUsuario(restTemplate, profesional.get("id").toString());
	}
}
