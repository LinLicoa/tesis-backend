package com.application.webapi.controller;

import com.application.webapi.service.ParametroSistemaService;
import com.application.webapi.service.dto.ParametroSistemaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/parametros-sistema")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class ParametroSistemaController {

    private final ParametroSistemaService parametroSistemaService;

    @GetMapping
    public ResponseEntity<List<ParametroSistemaDTO>> getAll() {
        return ResponseEntity.ok(parametroSistemaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParametroSistemaDTO> getById(@PathVariable UUID id) {
        return parametroSistemaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/clave/{clave}")
    public ResponseEntity<ParametroSistemaDTO> getByClave(@PathVariable String clave) {
        return parametroSistemaService.findByClave(clave)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParametroSistemaDTO> create(@Valid @RequestBody ParametroSistemaDTO dto) {
        return ResponseEntity.ok(parametroSistemaService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParametroSistemaDTO> update(@PathVariable UUID id,
            @Valid @RequestBody ParametroSistemaDTO dto) {
        return ResponseEntity.ok(parametroSistemaService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        parametroSistemaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
