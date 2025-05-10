package com.careassistant.orchestrator.config;

import com.careassistant.orchestrator.security.JWTUtility;
import com.careassistant.orchestrator.security.JwtAuthenticationEntryPoint;
import com.careassistant.orchestrator.security.JwtAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	private final JWTUtility jwtUtility;

	private final JwtAuthenticationEntryPoint authenticationEntryPoint;

	public SecurityConfig(JWTUtility jwtUtility, JwtAuthenticationEntryPoint authenticationEntryPoint) {
		this.jwtUtility = jwtUtility;
		this.authenticationEntryPoint = authenticationEntryPoint;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/auth/login").permitAll()
				.requestMatchers("/searches").hasRole("PACIENTE")
				.requestMatchers("/appointments/*/professional").hasRole("PROFESIONAL_SALUD")
				.requestMatchers("/appointments/*/confirm", "/appointments/*/cancel").hasRole("PROFESIONAL_SALUD")
				.anyRequest().authenticated()
			)
			.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint))
			.addFilterBefore(new JwtAuthenticationFilter(jwtUtility), UsernamePasswordAuthenticationFilter.class)
			.build();
	}

}
