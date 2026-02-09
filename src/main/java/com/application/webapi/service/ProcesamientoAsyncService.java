package com.application.webapi.service;

import com.application.webapi.service.dto.ResultadosEvaluacionDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ProcesamientoAsyncService {

    CompletableFuture<ResultadosEvaluacionDTO> procesarEvaluacionAsync(UUID evaluacionId);

    List<com.application.webapi.domain.entity.EvaluacionRecomendacion> asignarRecomendacionesPorIds(UUID evaluacionId,
            List<String> recomendacionIds,
            Map<String, String> criteriosSeleccion);
}
