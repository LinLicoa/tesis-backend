package com.application.webapi.service.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ConsultaMedicaDTO {
    private UUID id;
    private UUID pacienteId;
    private UUID usuarioId;
    private LocalDateTime fechaHora;

    // Signos Vitales (Triaje)
    private String presionArterial;
    private Integer frecuenciaCardiaca;
    private BigDecimal temperatura;
    private Integer saturacionOxigeno;
    private BigDecimal pesoKg;
    private Integer tallaCm;

    // Evolución Clínica
    private String motivoConsulta;
    private String examenFisico;
    private String diagnosticoCie10;
    private String diagnosticoDescripcion;
    private String planTratamiento;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
