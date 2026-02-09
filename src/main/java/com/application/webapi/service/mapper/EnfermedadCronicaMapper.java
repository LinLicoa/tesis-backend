package com.application.webapi.service.mapper;

import com.application.webapi.domain.entity.EnfermedadCronica;
import com.application.webapi.service.dto.EnfermedadCronicaDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EnfermedadCronicaMapper {

    public EnfermedadCronicaDTO toDto(EnfermedadCronica entity) {
        if (entity == null) {
            return null;
        }
        return EnfermedadCronicaDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public EnfermedadCronica toEntity(EnfermedadCronicaDTO dto) {
        if (dto == null) {
            return null;
        }
        return EnfermedadCronica.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .build();
    }

    public List<EnfermedadCronicaDTO> toDtoList(List<EnfermedadCronica> entities) {
        if (entities == null) {
            return java.util.Collections.emptyList();
        }
        return entities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
