package com.application.webapi.service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PacienteDTO {
    private UUID id;
    private String cedula;
    private String nombreEncriptado;
    private int edad;
    private String genero;
    private String enfermedadCronica;

    // Campos clínicos históricos
    private String tipoSangre;
    private String alergias;
    private String antecedentesFamiliares;
    private String ocupacion;

    private boolean activo;
    private LocalDateTime fechaRegistro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
