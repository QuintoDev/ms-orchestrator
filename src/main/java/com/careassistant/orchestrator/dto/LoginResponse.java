package com.careassistant.orchestrator.dto;

public class LoginResponse {

	private String token;

	public LoginResponse(String token, String correo, String rol) {
		this.token = token;
	}

	public String getToken() {
		return token;
	}

}
