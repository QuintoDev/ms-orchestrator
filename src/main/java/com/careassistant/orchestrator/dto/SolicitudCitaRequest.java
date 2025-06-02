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

	public void setUuidProfesionalSalud(String uuidProfesionalSalud) {
		this.uuidProfesionalSalud = uuidProfesionalSalud;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}
	
	

}
