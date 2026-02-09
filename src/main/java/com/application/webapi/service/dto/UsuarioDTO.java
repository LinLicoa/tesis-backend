package com.application.webapi.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UsuarioDTO {
    private UUID id;
    private String nombreCompleto;
    private String cedulaProfesional;
    private String especialidad;
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String rol;
    private boolean activo;
    private LocalDateTime fechaRegistro;
    private LocalDateTime ultimoAcceso;
}
