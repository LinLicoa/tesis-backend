package com.application.webapi.service;

import com.application.webapi.service.dto.EvaluacionDTO;
import com.application.webapi.service.dto.EstadoEvaluacionDTO;
import com.application.webapi.service.dto.RespuestasEvaluacionDTO;
import com.application.webapi.service.dto.ResultadosEvaluacionDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EvaluacionService {
    EvaluacionDTO create(EvaluacionDTO evaluacionDTO);

    Optional<EvaluacionDTO> findById(UUID id);

    List<EvaluacionDTO> findByPacienteId(UUID pacienteId);

    List<EvaluacionDTO> findByUsuarioId(UUID usuarioId);

    // New methods for async processing
    EstadoEvaluacionDTO submitRespuestas(UUID evaluacionId, RespuestasEvaluacionDTO respuestas);

    EstadoEvaluacionDTO getEstado(UUID evaluacionId);

    ResultadosEvaluacionDTO getResultados(UUID evaluacionId);

    RespuestasEvaluacionDTO getRespuestas(UUID evaluacionId);
}
