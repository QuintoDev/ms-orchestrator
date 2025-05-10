package com.careassistant.orchestrator.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.careassistant.orchestrator.dto.PacienteResponse;
import com.careassistant.orchestrator.dto.ProfesionalResponse;
import com.careassistant.orchestrator.dto.ProfesionalServiceResponse;

import java.util.Arrays;
import java.util.List;

@Service
public class ProfesionalService {

	private final RestTemplate restTemplate = new RestTemplate();

	@Value("${ms.users.url}")
	private String msUsersUrl;

	@Value("${ms.services.url}")
	private String msServicesUrl;

	public List<ProfesionalResponse> buscarProfesionales(String especialidad, String ciudad) {
		String url = msUsersUrl + "/users?especialidad=" + especialidad + "&ciudad=" + ciudad;
		ProfesionalResponse[] response = restTemplate.getForObject(url, ProfesionalResponse[].class);
		return Arrays.asList(response);
	}
	
	public List<ProfesionalServiceResponse> obtenerCitasProfesional(String uuidProfesional) {
	    String urlCitas = msServicesUrl + "/appointments/" + uuidProfesional + "/professional";
	    ProfesionalServiceResponse[] citasArray = restTemplate.getForObject(urlCitas, ProfesionalServiceResponse[].class);
	    if (citasArray == null) return List.of();

	    for (ProfesionalServiceResponse cita : citasArray) {
	        String pacienteUuid = cita.getUuidPaciente();

	        try {
	            String urlPaciente = msUsersUrl + "/users/" + pacienteUuid;
	            PacienteResponse paciente = restTemplate.getForObject(urlPaciente, PacienteResponse.class);
	            cita.setPaciente(paciente);
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
