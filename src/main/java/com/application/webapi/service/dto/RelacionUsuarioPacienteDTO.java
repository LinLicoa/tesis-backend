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
public class RelacionUsuarioPacienteDTO {
    private Integer id;
    private UUID usuarioId;
    private UUID pacienteId;
    private LocalDateTime fechaRelacion;
    private boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
