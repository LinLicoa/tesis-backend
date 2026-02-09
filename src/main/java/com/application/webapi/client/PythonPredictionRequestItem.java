package com.application.webapi.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PythonPredictionRequestItem {
    @JsonProperty("idcuestionario")
    private int idCuestionario;

    @JsonProperty("pregunta")
    private int pregunta;

    @JsonProperty("valor")
    private int valor;

    @JsonProperty("codigo_prueba")
    private String codigoPrueba;
}
