package com.application.webapi.service.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RecomendacionDTO {
    private UUID id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String dimensionAplicable;
    private String nivelMinimo;
    private String nivelMaximo;
    private Integer prioridad;
    private boolean esUrgente;
    private String evidenciaCientifica;
    private boolean activo;
    private java.time.LocalDateTime fechaCreacion;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;
}
