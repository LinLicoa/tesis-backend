package com.application.webapi.controller;

import com.application.webapi.service.PacienteService;
import com.application.webapi.service.RelacionUsuarioPacienteService;
import com.application.webapi.service.dto.PacienteDTO;
import com.application.webapi.service.dto.RelacionUsuarioPacienteDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final RelacionUsuarioPacienteService relacionService;

    @GetMapping
    public ResponseEntity<List<PacienteDTO>> getAll() {
        return ResponseEntity.ok(pacienteService.findAll());
    }

    @PostMapping
    public ResponseEntity<PacienteDTO> create(
            @Valid @RequestBody PacienteDTO pacienteDTO,
            @RequestParam UUID usuarioId) {
        return ResponseEntity.ok(pacienteService.create(pacienteDTO, usuarioId));
    }

    @PostMapping("/asociar")
    public ResponseEntity<RelacionUsuarioPacienteDTO> asociar(
            @RequestParam UUID pacienteId,
            @RequestParam UUID usuarioId) {
        return ResponseEntity.ok(relacionService.create(usuarioId, pacienteId));
    }

    @PostMapping("/desasociar")
    public ResponseEntity<Void> desasociar(
            @RequestParam UUID pacienteId,
            @RequestParam UUID usuarioId) {
        relacionService.deactivateByUsuarioAndPaciente(usuarioId, pacienteId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteDTO> getById(@PathVariable UUID id) {
        return pacienteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<PacienteDTO> getByCedula(@PathVariable String cedula) {
        return pacienteService.findByCedula(cedula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PacienteDTO>> getByUsuarioId(@PathVariable UUID usuarioId) {
        return ResponseEntity.ok(pacienteService.findByUsuarioId(usuarioId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        pacienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteDTO> update(@PathVariable UUID id, @RequestBody @Valid PacienteDTO dto) {
        return ResponseEntity.ok(pacienteService.update(id, dto));
    }
}
