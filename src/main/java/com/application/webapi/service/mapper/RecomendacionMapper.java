package com.application.webapi.service.mapper;

import com.application.webapi.domain.entity.Recomendacion;
import com.application.webapi.service.dto.RecomendacionDTO;
import org.springframework.stereotype.Component;

@Component
public class RecomendacionMapper {

    public RecomendacionDTO toDto(Recomendacion entity) {
        if (entity == null)
            return null;
        RecomendacionDTO dto = new RecomendacionDTO();
        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        dto.setDescripcion(entity.getDescripcion());
        dto.setCategoria(entity.getCategoria());
        dto.setDimensionAplicable(entity.getDimensionAplicable());
        dto.setNivelMinimo(entity.getNivelMinimo());
        dto.setNivelMaximo(entity.getNivelMaximo());
        dto.setPrioridad(entity.getPrioridad());
        dto.setEsUrgente(entity.isEsUrgente());
        dto.setEvidenciaCientifica(entity.getEvidenciaCientifica());
        dto.setActivo(entity.isActivo());
        dto.setFechaCreacion(entity.getFechaCreacion());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public Recomendacion toEntity(RecomendacionDTO dto) {
        if (dto == null)
            return null;
        Recomendacion entity = new Recomendacion();
        entity.setId(dto.getId());
        entity.setTitulo(dto.getTitulo());
        entity.setDescripcion(dto.getDescripcion());
        entity.setCategoria(dto.getCategoria());
        entity.setDimensionAplicable(dto.getDimensionAplicable());
        entity.setNivelMinimo(dto.getNivelMinimo());
        entity.setNivelMaximo(dto.getNivelMaximo());
        entity.setPrioridad(dto.getPrioridad());
        entity.setEsUrgente(dto.isEsUrgente());
        entity.setEvidenciaCientifica(dto.getEvidenciaCientifica());
        entity.setActivo(dto.isActivo());
        return entity;
    }
}
