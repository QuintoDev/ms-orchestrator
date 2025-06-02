package com.careassistant.orchestrator.util;

import com.careassistant.orchestrator.dto.UsuarioRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class TestUtils {

	public static Map<String, Object> crearPaciente(RestTemplate restTemplate, int port) {
		UsuarioRequest nuevo = new UsuarioRequest();
		nuevo.setTipo("PACIENTE");
		nuevo.setNombre("Test");
		nuevo.setApellido("Paciente");
		String correo = "paciente_" + UUID.randomUUID() + "@care.co";
		nuevo.setCorreo(correo);
		nuevo.setContraseña("12345678");
		nuevo.setCiudad("Bogotá");
		nuevo.setCelular(1234567890L);
		nuevo.setEdad(30);
		nuevo.setParentesco("Hijo");

		Map<String, Object> result = registrarUsuario(restTemplate, port, nuevo);
		result.put("correo", correo);

		return result;
	}

	public static Map<String, Object> crearProfesional(RestTemplate restTemplate, int port) {
		UsuarioRequest nuevo = new UsuarioRequest();
		nuevo.setTipo("PROFESIONAL_SALUD");
		nuevo.setNombre("Test");
		nuevo.setApellido("Profesional");
		String correo = "paciente_" + UUID.randomUUID() + "@care.co";
		nuevo.setCorreo(correo);
		nuevo.setContraseña("12345678");
		nuevo.setCiudad("Bogotá");
		nuevo.setEspecialidad("geriatria");
		nuevo.setDisponibilidad(List.of("Lunes", "Martes"));
		nuevo.setPresentacion("Soy un profesional de prueba para test.");

		Map<String, Object> result = registrarUsuario(restTemplate, port, nuevo);
		result.put("correo", correo);

		return result;
	}

	public static String loginPaciente(RestTemplate restTemplate, int port, Map<String, Object> paciente) {
		Object correoObj = paciente.get("correo");
		if (correoObj == null) {
			throw new IllegalArgumentException(
					"El objeto paciente no contiene la clave 'correo'. Mapa recibido: " + paciente);
		}
		return login(restTemplate, port, correoObj.toString(), "12345678");
	}

	public static String loginProfesional(RestTemplate restTemplate, int port, Map<String, Object> profesional) {
		return login(restTemplate, port, profesional.get("correo").toString(), "12345678");
	}

	public static String login(RestTemplate restTemplate, int port, String correo, String password) {
		Map<String, String> loginData = Map.of("correo", correo, "contraseña", password);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, String>> entity = new HttpEntity<>(loginData, headers);
		String url = "http://localhost:" + port + "/auth/login";

		ResponseEntity<Map<String, String>> response = restTemplate.exchange(url, HttpMethod.POST, entity,
				new ParameterizedTypeReference<Map<String, String>>() {
				});

		return response.getBody().get("token");
	}

	public static void eliminarUsuario(RestTemplate restTemplate, String uuid) {
		String url = "http://localhost:8081/users/" + uuid;
		restTemplate.delete(url);
	}

	public static String extraerUUIDDesdeToken(String token) {
		try {
			String[] partes = token.split("\\.");
			String payload = new String(Base64.getUrlDecoder().decode(partes[1]));
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> datos = mapper.readValue(payload, Map.class);
			return (String) datos.get("sub");
		} catch (Exception e) {
			throw new RuntimeException("No se pudo decodificar el token JWT", e);
		}
	}

	@SuppressWarnings("unchecked")
	private static Map<String, Object> registrarUsuario(RestTemplate restTemplate, int port, UsuarioRequest usuario) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UsuarioRequest> entity = new HttpEntity<>(usuario, headers);

		String url = "http://localhost:" + port + "/auth/signup";

		ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, entity,
				new ParameterizedTypeReference<Map<String, Object>>() {
				});

		return (Map<String, Object>) response.getBody().get("body");
	}
}
