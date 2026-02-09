package com.application.webapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EstadoEvaluacionDTO {

    private UUID evaluacionId;
    private String estado;
    private int progreso;
    private boolean completado;
    private String mensaje;
    private String resultadosUrl;
    private String error;
}
