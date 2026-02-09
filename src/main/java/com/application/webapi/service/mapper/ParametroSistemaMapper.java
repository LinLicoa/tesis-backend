package com.application.webapi.service.mapper;

import com.application.webapi.domain.entity.ParametroSistema;
import com.application.webapi.service.dto.ParametroSistemaDTO;
import org.springframework.stereotype.Component;

@Component
public class ParametroSistemaMapper {

    public ParametroSistemaDTO toDto(ParametroSistema entity) {
        if (entity == null) {
            return null;
        }
        return ParametroSistemaDTO.builder()
                .id(entity.getId())
                .clave(entity.getClave())
                .valor(entity.getValor())
                .tipoDato(entity.getTipoDato())
                .categoria(entity.getCategoria())
                .descripcion(entity.getDescripcion())
                .editable(entity.isEditable())
                .modificadoPorId(entity.getModificadoPor() != null ? entity.getModificadoPor().getId() : null)
                .fechaModificacion(entity.getFechaModificacion())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public ParametroSistema toEntity(ParametroSistemaDTO dto) {
        if (dto == null) {
            return null;
        }
        ParametroSistema entity = new ParametroSistema();
        entity.setId(dto.getId());
        entity.setClave(dto.getClave());
        entity.setValor(dto.getValor());
        entity.setTipoDato(dto.getTipoDato());
        entity.setCategoria(dto.getCategoria());
        entity.setDescripcion(dto.getDescripcion());
        entity.setEditable(dto.isEditable());
        // Note: modificadoPor is typically set by the service using a repository lookup
        return entity;
    }
}
