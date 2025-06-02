package com.careassistant.orchestrator;

import com.careassistant.orchestrator.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SignupSystemTest {

	@LocalServerPort
	private int port;

	private final RestTemplate restTemplate = new RestTemplate();

	@Test
	public void crearUsuarioPacienteTest() {
		Map<String, Object> paciente = TestUtils.crearPaciente(restTemplate, port);

		assertNotNull(paciente);
		assertEquals("PACIENTE", paciente.get("tipo"));
		assertNotNull(paciente.get("id"));

		TestUtils.eliminarUsuario(restTemplate, paciente.get("id").toString());
	}

	@Test
	public void crearUsuarioProfesionalTest() {
		Map<String, Object> profesional = TestUtils.crearProfesional(restTemplate, port);

		assertNotNull(profesional);
		assertEquals("PROFESIONAL_SALUD", profesional.get("tipo"));
		assertNotNull(profesional.get("id"));

		TestUtils.eliminarUsuario(restTemplate, profesional.get("id").toString());
	}
}
