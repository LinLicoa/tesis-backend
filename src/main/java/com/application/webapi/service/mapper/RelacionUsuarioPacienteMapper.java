package com.application.webapi.service.mapper;

import com.application.webapi.domain.entity.RelacionUsuarioPaciente;
import com.application.webapi.service.dto.RelacionUsuarioPacienteDTO;
import org.springframework.stereotype.Component;

@Component
public class RelacionUsuarioPacienteMapper {

    public RelacionUsuarioPacienteDTO toDto(RelacionUsuarioPaciente entity) {
        if (entity == null) {
            return null;
        }
        return RelacionUsuarioPacienteDTO.builder()
                .id(entity.getId())
                .usuarioId(entity.getUsuario() != null ? entity.getUsuario().getId() : null)
                .pacienteId(entity.getPaciente() != null ? entity.getPaciente().getId() : null)
                .fechaRelacion(entity.getFechaRelacion())
                .activo(entity.isActivo())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
