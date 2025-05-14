package com.careassistant.orchestrator.dto;

public class SolicitudCitaRequest {

	private String uuidProfesionalSalud;
	private String especialidad;
	private String fecha;
	private String hora;
	private String resumen;
	private String ubicacion;

	public String getUuidProfesionalSalud() {
		return uuidProfesionalSalud;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public String getFecha() {
		return fecha;
	}

	public String getHora() {
		return hora;
	}

	public String getResumen() {
		return resumen;
	}

	public String getUbicacion() {
		return ubicacion;
	}

}
