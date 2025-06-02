package com.careassistant.orchestrator;

import com.careassistant.orchestrator.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AssignAppointmentSystemTest {

	@LocalServerPort
	private int port;

	private final RestTemplate restTemplate = new RestTemplate();

	@Test
	public void asignarCitaTest() {
		// 1. Crear paciente y profesional
		Map<String, Object> paciente = TestUtils.crearPaciente(restTemplate, port);
		Map<String, Object> profesional = TestUtils.crearProfesional(restTemplate, port);

		// 2. Login para obtener token
		String token = TestUtils.loginPaciente(restTemplate, port, paciente);

		// 3. Preparar datos de cita
		Map<String, Object> cita = Map.of("fecha", "2025-05-11", "hora", "10:30", "uuidProfesionalSalud",
				profesional.get("id"), "resumen", "Este es un resumen de test", "ubicacion", "Casa");

		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(token);
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(cita, headers);
		String url = "http://localhost:" + port + "/appointments";

		ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, entity, Void.class);

		// Verifica solo el código de respuesta
		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		// 4. Eliminar usuarios
		TestUtils.eliminarUsuario(restTemplate, paciente.get("id").toString());
		TestUtils.eliminarUsuario(restTemplate, profesional.get("id").toString());
	}

}
