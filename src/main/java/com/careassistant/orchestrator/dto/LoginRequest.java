package com.careassistant.orchestrator.dto;

public class LoginRequest {

	private String correo;
	private String contraseña;
	
	public LoginRequest() {
     }

	public LoginRequest(String correo2, String contraseña2) {
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getContraseña() {
		return contraseña;
	}

	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}

}
