package com.application.webapi.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Request sent to Python service - Contains everything needed for processing
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PythonProcesamientoRequest {
    private String evaluacionId;
    private List<Map<String, Object>> respuestas;
    private Map<String, Integer> puntajes;
    private Map<String, String> niveles;
    private Map<String, Object> paciente;
}
