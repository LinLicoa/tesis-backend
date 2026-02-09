package com.application.webapi.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PythonPredictionResponseItem {
    @JsonProperty("idcuestionario")
    private int idCuestionario;

    @JsonProperty("detalles")
    private List<DetalleDTO> detalles;

    @JsonProperty("recomendaciones")
    private List<String> recomendaciones;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DetalleDTO {
        @JsonProperty("prueba")
        private String prueba;
        @JsonProperty("condicion")
        private String condicion;
        @JsonProperty("porcentaje")
        private double porcentaje;
        @JsonProperty("t")
        private double t;
        @JsonProperty("i")
        private double i;
        @JsonProperty("f")
        private double f;
    }
}
