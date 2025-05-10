package com.careassistant.orchestrator.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PacienteServiceResponse {

	private String id;

	private Date fecha;
	private String hora;
	private String estado;

	private ProfesionalResponse profesional;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String uuidProfesionalSalud;

	private String resumen;
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

	public String getUuidProfesionalSalud() {
		return uuidProfesionalSalud;
	}

	public String getResumen() {
		return resumen;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public ProfesionalResponse getProfesional() {
		return profesional;
	}

	public void setProfesional(ProfesionalResponse profesional) {
		this.profesional = profesional;
	}

}
