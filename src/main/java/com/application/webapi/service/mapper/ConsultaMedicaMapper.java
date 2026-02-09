package com.application.webapi.service.mapper;

import com.application.webapi.domain.entity.ConsultaMedica;
import com.application.webapi.service.dto.ConsultaMedicaDTO;
import org.springframework.stereotype.Component;

@Component
public class ConsultaMedicaMapper {

    public ConsultaMedicaDTO toDto(ConsultaMedica entity) {
        if (entity == null)
            return null;

        ConsultaMedicaDTO dto = new ConsultaMedicaDTO();
        dto.setId(entity.getId());
        dto.setPacienteId(entity.getPaciente() != null ? entity.getPaciente().getId() : null);
        dto.setUsuarioId(entity.getUsuario() != null ? entity.getUsuario().getId() : null);
        dto.setFechaHora(entity.getFechaHora());

        // Signos Vitales
        dto.setPresionArterial(entity.getPresionArterial());
        dto.setFrecuenciaCardiaca(entity.getFrecuenciaCardiaca());
        dto.setTemperatura(entity.getTemperatura());
        dto.setSaturacionOxigeno(entity.getSaturacionOxigeno());
        dto.setPesoKg(entity.getPesoKg());
        dto.setTallaCm(entity.getTallaCm());

        // Evolución Clínica
        dto.setMotivoConsulta(entity.getMotivoConsulta());
        dto.setExamenFisico(entity.getExamenFisico());
        dto.setDiagnosticoCie10(entity.getDiagnosticoCie10());
        dto.setDiagnosticoDescripcion(entity.getDiagnosticoDescripcion());
        dto.setPlanTratamiento(entity.getPlanTratamiento());

        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());

        return dto;
    }

    public ConsultaMedica toEntity(ConsultaMedicaDTO dto) {
        if (dto == null)
            return null;

        ConsultaMedica entity = new ConsultaMedica();
        entity.setId(dto.getId());
        entity.setFechaHora(dto.getFechaHora() != null ? dto.getFechaHora() : java.time.LocalDateTime.now());

        // Signos Vitales
        entity.setPresionArterial(dto.getPresionArterial());
        entity.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        entity.setTemperatura(dto.getTemperatura());
        entity.setSaturacionOxigeno(dto.getSaturacionOxigeno());
        entity.setPesoKg(dto.getPesoKg());
        entity.setTallaCm(dto.getTallaCm());

        // Evolución Clínica
        entity.setMotivoConsulta(dto.getMotivoConsulta());
        entity.setExamenFisico(dto.getExamenFisico());
        entity.setDiagnosticoCie10(dto.getDiagnosticoCie10());
        entity.setDiagnosticoDescripcion(dto.getDiagnosticoDescripcion());
        entity.setPlanTratamiento(dto.getPlanTratamiento());

        // Relationships set in service
        return entity;
    }

    public void updateEntityFromDto(ConsultaMedicaDTO dto, ConsultaMedica entity) {
        if (dto.getPresionArterial() != null)
            entity.setPresionArterial(dto.getPresionArterial());
        if (dto.getFrecuenciaCardiaca() != null)
            entity.setFrecuenciaCardiaca(dto.getFrecuenciaCardiaca());
        if (dto.getTemperatura() != null)
            entity.setTemperatura(dto.getTemperatura());
        if (dto.getSaturacionOxigeno() != null)
            entity.setSaturacionOxigeno(dto.getSaturacionOxigeno());
        if (dto.getPesoKg() != null)
            entity.setPesoKg(dto.getPesoKg());
        if (dto.getTallaCm() != null)
            entity.setTallaCm(dto.getTallaCm());
        if (dto.getMotivoConsulta() != null)
            entity.setMotivoConsulta(dto.getMotivoConsulta());
        if (dto.getExamenFisico() != null)
            entity.setExamenFisico(dto.getExamenFisico());
        if (dto.getDiagnosticoCie10() != null)
            entity.setDiagnosticoCie10(dto.getDiagnosticoCie10());
        if (dto.getDiagnosticoDescripcion() != null)
            entity.setDiagnosticoDescripcion(dto.getDiagnosticoDescripcion());
        if (dto.getPlanTratamiento() != null)
            entity.setPlanTratamiento(dto.getPlanTratamiento());
    }
}
