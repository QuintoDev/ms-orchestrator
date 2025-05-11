package com.careassistant.orchestrator.dto;

public class PacienteResponse {

	private String nombre;
	private String apellido;
	private String parentesco;
	private Integer edad;
	private Long celular;

	public String getNombre() {
		return nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public String getParentesco() {
		return parentesco;
	}

	public Integer getEdad() {
		return edad;
	}

	public Long getCelular() {
		return celular;
	}

}
