package com.application.webapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResultadosEvaluacionDTO {

    private UUID evaluacionId;
    private LocalDateTime fechaEvaluacion;

    // Información del paciente
    private PacienteInfo paciente;

    // Puntajes calculados
    private Map<String, Integer> puntajes;

    // Niveles clasificados
    private Map<String, String> niveles;

    // Tripletas globales
    private Map<String, TripletaGlobalDTO> tripletasGlobales;

    // Probabilidades de adherencia
    private ProbabilidadesDTO probabilidadesAdherencia;

    // Recomendaciones asignadas
    // Recomendaciones asignadas (Texto simple)
    private List<String> recomendaciones;

    // Alerta crítica
    private String alertaCritica;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PacienteInfo {
        private UUID id;
        private int edad;
        private String genero;
        private String enfermedadCronica;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TripletaGlobalDTO {
        @com.fasterxml.jackson.annotation.JsonProperty("t")
        private BigDecimal T;
        @com.fasterxml.jackson.annotation.JsonProperty("i")
        private BigDecimal I;
        @com.fasterxml.jackson.annotation.JsonProperty("f")
        private BigDecimal F;
        @com.fasterxml.jackson.annotation.JsonProperty("t_bruto")
        private BigDecimal TBruto;
        @com.fasterxml.jackson.annotation.JsonProperty("i_bruto")
        private BigDecimal IBruto;
        @com.fasterxml.jackson.annotation.JsonProperty("f_bruto")
        private BigDecimal FBruto;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProbabilidadesDTO {
        private BigDecimal ansiedad;
        private BigDecimal depresion;
        private BigDecimal estres;
    }

    // DTO class removed as we are using simple Strings now
}
