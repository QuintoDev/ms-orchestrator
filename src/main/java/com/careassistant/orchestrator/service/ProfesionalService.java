package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.BuscarProfesionalResponse;
import com.careassistant.orchestrator.dto.PacienteResponse;
import com.careassistant.orchestrator.dto.ServiciosServiceResponse;
import com.careassistant.orchestrator.security.AES256Decriptor;
import com.careassistant.orchestrator.security.JWTUtility;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class ProfesionalService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final AES256Decriptor aes;
	private final JWTUtility jwtUtility;

	public ProfesionalService(AES256Decriptor aes, JWTUtility jwtUtility) {
		super();
		this.aes = aes;
		this.jwtUtility = jwtUtility;
	}

	@Value("${ms.users.url}")
	private String msUsersUrl;

	@Value("${ms.services.url}")
	private String msServicesUrl;

	public List<ServiciosServiceResponse> obtenerCitasProfesional(String uuidProfesional, String token)
			throws Exception {

		UUID uuidDelToken = jwtUtility.obtenerUserId(token);

		if (!uuidProfesional.equals(uuidDelToken.toString())) {
			throw new SecurityException("Acceso no autorizado");
		}

		String urlCitas = msServicesUrl + "/appointments/" + uuidProfesional + "/professional";
		ServiciosServiceResponse[] citasArray = restTemplate.getForObject(urlCitas, ServiciosServiceResponse[].class);
		if (citasArray == null)
			return List.of();

		for (ServiciosServiceResponse cita : citasArray) {
			String pacienteUuid = cita.getUuidPaciente();
			String decifrado = aes.decrypt(cita.getResumen());

			try {
				String urlPaciente = msUsersUrl + "/users/" + pacienteUuid;
				PacienteResponse paciente = restTemplate.getForObject(urlPaciente, PacienteResponse.class);
				cita.setPaciente(paciente);
				cita.setResumen(decifrado);
			} catch (Exception e) {
				System.out.println("Error obteniendo paciente " + pacienteUuid + ": " + e.getMessage());
			}
		}

		return Arrays.asList(citasArray);
	}

	public List<BuscarProfesionalResponse> buscarProfesionales(String especialidad, String ciudad) throws Exception {
		String url = msUsersUrl + "/users?especialidad=" + especialidad + "&ciudad=" + ciudad;

		BuscarProfesionalResponse[] profesionalesArray = restTemplate.getForObject(url,
				BuscarProfesionalResponse[].class);

		if (profesionalesArray == null)
			return List.of();

		for (BuscarProfesionalResponse profesional : profesionalesArray) {
			String decifrado = aes.decrypt(profesional.getPresentacion());
			profesional.setPresentacion(decifrado);

		}

		return Arrays.asList(profesionalesArray);
	}

	public void confirmarCita(String idCita, String token) throws Exception {
		validarAccesoDelProfesionalALaCita(idCita, token);
		String url = msServicesUrl + "/appointments/" + idCita + "/confirm";
		restTemplate.put(url, null);
	}

	public void cancelarCita(String idCita, String token) throws Exception {
		validarAccesoDelProfesionalALaCita(idCita, token);
		String url = msServicesUrl + "/appointments/" + idCita + "/cancel";
		restTemplate.put(url, null);
	}

	private void validarAccesoDelProfesionalALaCita(String idCita, String token) throws Exception {
		UUID uuidDelToken = jwtUtility.obtenerUserId(token);

		String url = msServicesUrl + "/appointments/" + idCita;
		ServiciosServiceResponse cita = restTemplate.getForObject(url, ServiciosServiceResponse.class);

		if (cita == null) {
			throw new IllegalArgumentException("La cita no existe");
		}

		if (!uuidDelToken.toString().equals(cita.getUuidProfesionalSalud())) {
			throw new SecurityException("Acceso no autorizado: el profesional no coincide");
		}
	}

}
