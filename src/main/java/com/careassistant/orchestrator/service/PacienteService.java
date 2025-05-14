package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.PacienteServiceResponse;
import com.careassistant.orchestrator.dto.ProfesionalResponse;
import com.careassistant.orchestrator.security.AES256Decriptor;

import java.util.Arrays;
import java.util.List;

@Service
public class PacienteService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final AES256Decriptor aes;

	public PacienteService(AES256Decriptor aes) {
		super();
		this.aes = aes;
	}

	@Value("${ms.users.url}")
	private String msUsersUrl;

	@Value("${ms.services.url}")
	private String msServicesUrl;

	public List<PacienteServiceResponse> obtenerCitasPaciente(String uuidPaciente) throws Exception {
		String urlCitas = msServicesUrl + "/appointments/" + uuidPaciente + "/patient";
		PacienteServiceResponse[] citasArray = restTemplate.getForObject(urlCitas, PacienteServiceResponse[].class);
		if (citasArray == null)
			return List.of();

		for (PacienteServiceResponse cita : citasArray) {
			String profesionalUuid = cita.getUuidProfesionalSalud();
			String decifrado = aes.decrypt(cita.getResumen());

			try {
				String urlPaciente = msUsersUrl + "/users/" + profesionalUuid;
				ProfesionalResponse profesional = restTemplate.getForObject(urlPaciente, ProfesionalResponse.class);
				cita.setProfesional(profesional);
				cita.setResumen(decifrado);
			} catch (Exception e) {
				System.out.println("Error obteniendo paciente " + profesionalUuid + ": " + e.getMessage());
			}
		}

		return Arrays.asList(citasArray);
	}

}
