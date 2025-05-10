package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.PacienteResponse;
import com.careassistant.orchestrator.dto.PacienteServiceResponse;
import com.careassistant.orchestrator.dto.ProfesionalResponse;
import com.careassistant.orchestrator.dto.ProfesionalServiceResponse;

import java.util.Arrays;
import java.util.List;

@Service
public class PacienteService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${ms.users.url}")
	private String msUsersUrl;

	@Value("${ms.services.url}")
	private String msServicesUrl;

	public List<PacienteServiceResponse> obtenerCitasPaciente(String uuidPaciente) {
		String urlCitas = msServicesUrl + "/appointments/" + uuidPaciente + "/patient";
		PacienteServiceResponse[] citasArray = restTemplate.getForObject(urlCitas, PacienteServiceResponse[].class);
		if (citasArray == null)
			return List.of();

		for (PacienteServiceResponse cita : citasArray) {
			String profesionalUuid = cita.getUuidProfesionalSalud();

			try {
				String urlPaciente = msUsersUrl + "/users/" + profesionalUuid;
				ProfesionalResponse profesional = restTemplate.getForObject(urlPaciente, ProfesionalResponse.class);
				cita.setProfesional(profesional);
			} catch (Exception e) {
				System.out.println("Error obteniendo paciente " + profesionalUuid + ": " + e.getMessage());
			}
		}

		return Arrays.asList(citasArray);
	}

}
