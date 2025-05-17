package com.app.usuarios.Config;

import com.app.usuarios.Config.Dto.TokenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthClientService {
    private final RestTemplate restTemplate;
    private final String AUTH_SERVICE_URL = "http://localhost:9010/api/auth/validate-token";

    public AuthClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public boolean validateToken(String token) {
        try {
            // Crea el cuerpo de la solicitud
            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("token", token);

            // Realiza la petición
            ResponseEntity<TokenResponse> response = restTemplate.postForEntity(
                    AUTH_SERVICE_URL,
                    requestBody,
                    TokenResponse.class
            );

            return response.getStatusCode().is2xxSuccessful();

        } catch (HttpClientErrorException e) {
            // Maneja errores 4xx
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new BadCredentialsException("Token inválido");
            }
            throw new RuntimeException("Error de comunicación con el servicio de autenticación");
        }
    }
}
