package com.careassistant.orchestrator.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfesionalServiceResponse {

	private String id;

	private Date fecha;
	private String hora;
	private String estado;

	private PacienteResponse paciente;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String uuidPaciente;

	private String especialidad;
	private String ubicacion;

	public String getId() {
		return id;
	}

	public Date getFecha() {
		return fecha;
	}

	public String getHora() {
		return hora;
	}

	public String getEstado() {
		return estado;
	}

	public String getUuidPaciente() {
		return uuidPaciente;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public PacienteResponse getPaciente() {
		return paciente;
	}

	public void setPaciente(PacienteResponse paciente) {
		this.paciente = paciente;
	}

}
