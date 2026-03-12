package com.application.webapi.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class PythonServiceClient {

    private final RestTemplate restTemplate;
    private final String pythonServiceUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PythonServiceClient(
            RestTemplate restTemplate,
            @Value("${python.service.url:http://127.0.0.1:8000}") String pythonServiceUrl) {
        this.restTemplate = restTemplate;
        this.pythonServiceUrl = pythonServiceUrl;
    }

    public <T, R> R post(String endpoint, T request, Class<R> responseType) {
        String url = pythonServiceUrl + endpoint;
        log.info("Calling Python service: POST {}", url);

        // Log the request body for debugging
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            log.info(">>> Python request body: {}", requestJson);
        } catch (Exception e) {
            log.warn("Could not serialize request for logging: {}", e.getMessage());
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<T> entity = new HttpEntity<>(request, headers);

            ResponseEntity<R> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    responseType);

            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Python service responded successfully");
                // Log the response body for debugging
                try {
                    String responseJson = objectMapper.writeValueAsString(response.getBody());
                    log.info("<<< Python response body: {}", responseJson);
                } catch (Exception e) {
                    log.warn("Could not serialize response for logging: {}", e.getMessage());
                }
                return response.getBody();
            } else {
                log.error("Python service returned error: {}", response.getStatusCode());
                throw new RuntimeException("Python service error: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            log.error("Error calling Python service: {}", e.getMessage());
            throw new RuntimeException("Error calling Python service: " + e.getMessage(), e);
        }
    }
}
