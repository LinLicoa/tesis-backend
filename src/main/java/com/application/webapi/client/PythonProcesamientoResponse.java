package com.application.webapi.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Response received from Python service - Contains everything processed
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PythonProcesamientoResponse {
    private List<TripletaIndividualDTO> tripletasIndividuales;
    private Map<String, TripletaGlobalDTO> tripletasGlobales;
    private ProbabilidadesDTO probabilidadesAdherencia;
    private List<String> recomendacionesIds;
    private Map<String, String> criteriosSeleccion;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TripletaIndividualDTO {
        private String cuestionario;
        @com.fasterxml.jackson.annotation.JsonProperty("numero_item")
        private int numeroItem;
        @com.fasterxml.jackson.annotation.JsonProperty("valor_ajustado")
        private Integer valorAjustado;
        private TripletaValores tripleta;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TripletaValores {
        private double t;
        private double i;
        private double f;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TripletaGlobalDTO {
        private double t;
        private double i;
        private double f;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ProbabilidadesDTO {
        private double alta;
        private double media;
        private double baja;
    }
}
