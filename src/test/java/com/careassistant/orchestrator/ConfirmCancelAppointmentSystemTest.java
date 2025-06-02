package com.careassistant.orchestrator;

import com.careassistant.orchestrator.util.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConfirmCancelAppointmentSystemTest {

    @LocalServerPort
    private int port;

    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    public void confirmarYCancelarCita_deberiaFuncionar() {
        // 1. Crear paciente y profesional
        Map<String, Object> paciente = TestUtils.crearPaciente(restTemplate, port);
        Map<String, Object> profesional = TestUtils.crearProfesional(restTemplate, port);

        // 2. Hacer login y obtener tokens
        String tokenPaciente = TestUtils.loginPaciente(restTemplate, port, paciente);
        String tokenProfesional = TestUtils.loginProfesional(restTemplate, port, profesional);

        // 3. Asignar una cita como paciente
        HttpHeaders headersPaciente = new HttpHeaders();
        headersPaciente.setBearerAuth(tokenPaciente);
        headersPaciente.setContentType(MediaType.APPLICATION_JSON);

        String urlAsignar = "http://localhost:" + port + "/appointments";

        Map<String, Object> body = Map.of(
                "fecha", "2025-06-02",
                "hora", "15:30",
                "uuidProfesionalSalud", profesional.get("id"),
                "resumen", "Prueba funcional de sistema",
                "ubicacion", "Casa"
        );

        HttpEntity<Map<String, Object>> entityAsignacion = new HttpEntity<>(body, headersPaciente);
        ResponseEntity<Void> responseAsignacion = restTemplate.exchange(urlAsignar, HttpMethod.POST, entityAsignacion, Void.class);

        assertEquals(HttpStatus.CREATED, responseAsignacion.getStatusCode());

        // 4. Obtener citas del profesional
        String uuidProfesional = TestUtils.extraerUUIDDesdeToken(tokenProfesional);
        String urlCitasProfesional = "http://localhost:" + port + "/appointments/" + uuidProfesional + "/professional";

        HttpHeaders headersProfesional = new HttpHeaders();
        headersProfesional.setBearerAuth(tokenProfesional);
        HttpEntity<Void> entityProfesional = new HttpEntity<>(headersProfesional);

        ResponseEntity<List<Map<String, Object>>> respuestaProfesional = restTemplate.exchange(
                urlCitasProfesional,
                HttpMethod.GET,
                entityProfesional,
                new ParameterizedTypeReference<>() {}
        );

        assertEquals(HttpStatus.OK, respuestaProfesional.getStatusCode());
        assertNotNull(respuestaProfesional.getBody());
        assertFalse(respuestaProfesional.getBody().isEmpty());

        String citaId = respuestaProfesional.getBody().get(0).get("id").toString();

        // 5. Confirmar la cita
        String urlConfirmar = "http://localhost:" + port + "/appointments/" + citaId + "/confirm";
        ResponseEntity<Void> confirmResp = restTemplate.exchange(urlConfirmar, HttpMethod.PUT, entityProfesional, Void.class);
        assertEquals(HttpStatus.OK, confirmResp.getStatusCode());

        // 6. Cancelar la cita
        String urlCancelar = "http://localhost:" + port + "/appointments/" + citaId + "/cancel";
        ResponseEntity<Void> cancelResp = restTemplate.exchange(urlCancelar, HttpMethod.PUT, entityProfesional, Void.class);
        assertEquals(HttpStatus.OK, cancelResp.getStatusCode());

        // Cleanup
        TestUtils.eliminarUsuario(restTemplate, paciente.get("id").toString());
        TestUtils.eliminarUsuario(restTemplate, profesional.get("id").toString());
    }
}
