package com.application.webapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametroSistemaDTO {
    private UUID id;
    private String clave;
    private String valor;
    private String tipoDato;
    private String categoria;
    private String descripcion;
    private boolean editable;
    private UUID modificadoPorId;
    private LocalDateTime fechaModificacion;
    private LocalDateTime createdAt;
}
