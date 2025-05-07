package com.careassistant.orchestrator.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTUtility {

	@Value("${jwt.secret}")
	private String secretKey;

	private Key key;

	private static final long EXPIRATION_MS = 86400000;

	@PostConstruct
	public void init() {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generarToken(String correo, String rol) {
		return Jwts.builder().setSubject(correo).claim("rol", rol).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public boolean validarToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (JwtException e) {
			// log.error("Token inválido: {}", e.getMessage());
			return false;
		}
	}

	public String obtenerCorreo(String token) {
		return getClaims(token).getSubject();
	}

	public String obtenerRol(String token) {
		return getClaims(token).get("rol", String.class);
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
}
