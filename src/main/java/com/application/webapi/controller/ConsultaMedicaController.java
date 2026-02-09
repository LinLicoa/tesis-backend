package com.application.webapi.controller;

import com.application.webapi.service.ConsultaMedicaService;
import com.application.webapi.service.dto.ConsultaMedicaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/consultas-medicas")
@RequiredArgsConstructor
public class ConsultaMedicaController {

    private final ConsultaMedicaService consultaMedicaService;

    @PostMapping
    public ResponseEntity<ConsultaMedicaDTO> create(
            @Valid @RequestBody ConsultaMedicaDTO dto,
            @RequestParam UUID pacienteId,
            @RequestParam UUID usuarioId) {
        return ResponseEntity.ok(consultaMedicaService.create(dto, pacienteId, usuarioId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaMedicaDTO> getById(@PathVariable UUID id) {
        return consultaMedicaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{pacienteId}")
    public ResponseEntity<List<ConsultaMedicaDTO>> getByPacienteId(@PathVariable UUID pacienteId) {
        return ResponseEntity.ok(consultaMedicaService.findByPacienteId(pacienteId));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ConsultaMedicaDTO>> getByUsuarioId(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(consultaMedicaService.findByUsuarioId(usuarioId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ConsultaMedicaDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody ConsultaMedicaDTO dto) {
        return ResponseEntity.ok(consultaMedicaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        consultaMedicaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
