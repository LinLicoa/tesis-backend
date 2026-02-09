package com.application.webapi.service.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class EvaluacionDTO {
    private UUID id;
    private UUID pacienteId;
    private UUID usuarioId;
    private UUID consultaId;
    private LocalDateTime fechaHora;
    private String estado;
    private Integer gad7Puntaje;
    private Integer phq9Puntaje;
    private Integer pss10Puntaje;
    private String nivelAnsiedad;
    private String nivelDepresion;
    private String nivelEstres;
    private BigDecimal ansiedadT;
    private BigDecimal ansiedadI;
    private BigDecimal ansiedadF;
    private BigDecimal depresionT;
    private BigDecimal depresionI;
    private BigDecimal depresionF;
    private BigDecimal estresT;
    private BigDecimal estresI;
    private BigDecimal estresF;
    private BigDecimal probAdherenciaAlta;
    private BigDecimal probAdherenciaMedia;
    private BigDecimal probAdherenciaBaja;
    private String observaciones;
    private Integer tiempoProcesamiento;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
