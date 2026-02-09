package com.application.webapi.controller;

import com.application.webapi.service.EvaluacionService;
import com.application.webapi.service.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @PostMapping
    public ResponseEntity<EvaluacionDTO> create(@Valid @RequestBody EvaluacionDTO evaluacionDTO) {
        return ResponseEntity.ok(evaluacionService.create(evaluacionDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EvaluacionDTO> getById(@PathVariable UUID id) {
        return evaluacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<EvaluacionDTO>> getByPacienteId(@PathVariable UUID pacienteId) {
        return ResponseEntity.ok(evaluacionService.findByPacienteId(pacienteId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<EvaluacionDTO>> getByUsuarioId(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(evaluacionService.findByUsuarioId(usuarioId));
    }

    /**
     * Submit questionnaire responses and trigger async processing
     * Returns 202 Accepted
     */
    @PostMapping("/{id}/respuestas")
    public ResponseEntity<EstadoEvaluacionDTO> submitRespuestas(
            @PathVariable UUID id,
            @Valid @RequestBody RespuestasEvaluacionDTO respuestas) {
        EstadoEvaluacionDTO estado = evaluacionService.submitRespuestas(id, respuestas);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(estado);
    }

    /**
     * Poll evaluation status
     */
    @GetMapping("/{id}/estado")
    public ResponseEntity<EstadoEvaluacionDTO> getEstado(@PathVariable UUID id) {
        return ResponseEntity.ok(evaluacionService.getEstado(id));
    }

    /**
     * Get complete evaluation results
     * Only available when estado = "completada"
     */
    @GetMapping("/{id}/resultados")
    public ResponseEntity<ResultadosEvaluacionDTO> getResultados(@PathVariable UUID id) {
        return ResponseEntity.ok(evaluacionService.getResultados(id));
    }

    /**
     * Get submitted answers for an evaluation
     */
    @GetMapping("/{id}/respuestas")
    public ResponseEntity<RespuestasEvaluacionDTO> getRespuestas(@PathVariable UUID id) {
        return ResponseEntity.ok(evaluacionService.getRespuestas(id));
    }
}
