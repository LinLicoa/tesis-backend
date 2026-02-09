package com.application.webapi.client;

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

    public PythonServiceClient(
            RestTemplate restTemplate,
            @Value("${python.service.url:http://127.0.0.1:8000}") String pythonServiceUrl) {
        this.restTemplate = restTemplate;
        this.pythonServiceUrl = pythonServiceUrl;
    }

    public <T, R> R post(String endpoint, T request, Class<R> responseType) {
        String url = pythonServiceUrl + endpoint;
        log.info("Calling Python service: POST {}", url);

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
