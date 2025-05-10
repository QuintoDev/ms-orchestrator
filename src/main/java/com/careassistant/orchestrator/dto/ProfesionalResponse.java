package com.careassistant.orchestrator.dto;

import java.util.UUID;

public class ProfesionalResponse {
	private UUID id;
	private String rol;
	private String nombre;
	private String especialidad;
	private String ciudad;
	private String correo;
	private String presentacion;
	private String disponibilidad;
	
	public UUID getId() {
		return id;
	}

	public String getRol() {
		return rol;
	}

	public String getNombre() {
		return nombre;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getCorreo() {
		return correo;
	}

	public String getPresentacion() {
		return presentacion;
	}

	public String getDisponibilidad() {
		return disponibilidad;
	}

}