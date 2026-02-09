package com.application.webapi.service.mapper;

import com.application.webapi.domain.entity.Evaluacion;
import com.application.webapi.service.dto.EvaluacionDTO;
import org.springframework.stereotype.Component;

@Component
public class EvaluacionMapper {

    public EvaluacionDTO toDto(Evaluacion entity) {
        if (entity == null)
            return null;
        EvaluacionDTO dto = new EvaluacionDTO();
        dto.setId(entity.getId());
        if (entity.getPaciente() != null)
            dto.setPacienteId(entity.getPaciente().getId());
        if (entity.getUsuario() != null)
            dto.setUsuarioId(entity.getUsuario().getId());
        if (entity.getConsulta() != null)
            dto.setConsultaId(entity.getConsulta().getId());
        dto.setFechaHora(entity.getFechaHora());
        dto.setEstado(entity.getEstado());
        dto.setGad7Puntaje(entity.getGad7Puntaje());
        dto.setPhq9Puntaje(entity.getPhq9Puntaje());
        dto.setPss10Puntaje(entity.getPss10Puntaje());
        dto.setNivelAnsiedad(entity.getNivelAnsiedad());
        dto.setNivelDepresion(entity.getNivelDepresion());
        dto.setNivelEstres(entity.getNivelEstres());
        dto.setAnsiedadT(entity.getAnsiedadT());
        dto.setAnsiedadI(entity.getAnsiedadI());
        dto.setAnsiedadF(entity.getAnsiedadF());
        dto.setDepresionT(entity.getDepresionT());
        dto.setDepresionI(entity.getDepresionI());
        dto.setDepresionF(entity.getDepresionF());
        dto.setEstresT(entity.getEstresT());
        dto.setEstresI(entity.getEstresI());
        dto.setEstresF(entity.getEstresF());
        dto.setProbAdherenciaAlta(entity.getPorcentajeAnsiedad());
        dto.setProbAdherenciaMedia(entity.getPorcentajeDepresion());
        dto.setProbAdherenciaBaja(entity.getPorcentajeEstres());
        dto.setObservaciones(entity.getObservaciones());
        dto.setTiempoProcesamiento(entity.getTiempoProcesamiento());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public Evaluacion toEntity(EvaluacionDTO dto) {
        if (dto == null)
            return null;
        Evaluacion entity = new Evaluacion();
        entity.setId(dto.getId());
        if (dto.getFechaHora() != null) {
            entity.setFechaHora(dto.getFechaHora());
        }
        entity.setEstado(dto.getEstado());
        entity.setGad7Puntaje(dto.getGad7Puntaje());
        entity.setPhq9Puntaje(dto.getPhq9Puntaje());
        entity.setPss10Puntaje(dto.getPss10Puntaje());
        entity.setNivelAnsiedad(dto.getNivelAnsiedad());
        entity.setNivelDepresion(dto.getNivelDepresion());
        entity.setNivelEstres(dto.getNivelEstres());
        entity.setAnsiedadT(dto.getAnsiedadT());
        entity.setAnsiedadI(dto.getAnsiedadI());
        entity.setAnsiedadF(dto.getAnsiedadF());
        entity.setDepresionT(dto.getDepresionT());
        entity.setDepresionI(dto.getDepresionI());
        entity.setDepresionF(dto.getDepresionF());
        entity.setEstresT(dto.getEstresT());
        entity.setEstresI(dto.getEstresI());
        entity.setEstresF(dto.getEstresF());
        entity.setPorcentajeAnsiedad(dto.getProbAdherenciaAlta());
        entity.setPorcentajeDepresion(dto.getProbAdherenciaMedia());
        entity.setPorcentajeEstres(dto.getProbAdherenciaBaja());
        entity.setObservaciones(dto.getObservaciones());
        entity.setTiempoProcesamiento(dto.getTiempoProcesamiento());
        return entity;
    }
}
