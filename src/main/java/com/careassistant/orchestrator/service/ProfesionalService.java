package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.PacienteResponse;
import com.careassistant.orchestrator.dto.ProfesionalResponse;
import com.careassistant.orchestrator.dto.ProfesionalServiceResponse;
import com.careassistant.orchestrator.security.AES256Decriptor;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfesionalService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final AES256Decriptor aes;

	public ProfesionalService(AES256Decriptor aes) {
		super();
		this.aes = aes;
	}

	@Value("${ms.users.url}")
	private String msUsersUrl;

	@Value("${ms.services.url}")
	private String msServicesUrl;

	public List<ProfesionalServiceResponse> obtenerCitasProfesional(String uuidProfesional) throws Exception {
		String urlCitas = msServicesUrl + "/appointments/" + uuidProfesional + "/professional";
		ProfesionalServiceResponse[] citasArray = restTemplate.getForObject(urlCitas,
				ProfesionalServiceResponse[].class);
		if (citasArray == null)
			return List.of();

		for (ProfesionalServiceResponse cita : citasArray) {
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

	public void confirmarCita(String id) {
		String url = msServicesUrl + "/appointments/" + id + "/confirm";
		restTemplate.put(url, null);
	}

	public void cancelarCita(String id) {
		String url = msServicesUrl + "/appointments/" + id + "/cancel";
		restTemplate.put(url, null);
	}
}
