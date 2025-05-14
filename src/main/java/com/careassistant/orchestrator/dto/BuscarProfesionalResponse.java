package com.careassistant.orchestrator.dto;

import java.util.List;
import java.util.UUID;

public class BuscarProfesionalResponse {
	private UUID id;
	private String nombre;
	private String apellido;
	private String especialidad;
	private String ciudad;
	private String presentacion;
	private List<String> disponibilidad;

	public UUID getId() {
		return id;
	}

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getEspecialidad() {
		return especialidad;
	}

	public String getCiudad() {
		return ciudad;
	}

	public String getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(String presentacion) {
		this.presentacion = presentacion;
	}

	public List<String> getDisponibilidad() {
		return disponibilidad;
	}

}