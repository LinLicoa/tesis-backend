package com.application.webapi.service.mapper;

import com.application.webapi.service.dto.UsuarioDTO;
import com.application.webapi.domain.entity.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO toDto(Usuario entity) {
        if (entity == null) {
            return null;
        }
        return UsuarioDTO.builder()
                .id(entity.getId())
                .nombreCompleto(entity.getNombreCompleto())
                .cedulaProfesional(entity.getCedulaProfesional())
                .especialidad(entity.getEspecialidad())
                .email(entity.getEmail())
                .rol(entity.getRol())
                .activo(entity.isActivo())
                .fechaRegistro(entity.getFechaRegistro())
                .ultimoAcceso(entity.getUltimoAcceso())
                .build();
    }

    public Usuario toEntity(UsuarioDTO dto) {
        if (dto == null) {
            return null;
        }
        return Usuario.builder()
                .id(dto.getId())
                .nombreCompleto(dto.getNombreCompleto())
                .cedulaProfesional(dto.getCedulaProfesional())
                .especialidad(dto.getEspecialidad())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .rol(dto.getRol())
                .activo(dto.isActivo())
                .build();
    }
}
