package com.application.webapi.controller;

import com.application.webapi.service.EnfermedadCronicaService;
import com.application.webapi.service.dto.EnfermedadCronicaDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enfermedades-cronicas")
@RequiredArgsConstructor
public class EnfermedadCronicaController {

    private final EnfermedadCronicaService service;

    @GetMapping
    public ResponseEntity<List<EnfermedadCronicaDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnfermedadCronicaDTO> getById(@PathVariable Integer id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EnfermedadCronicaDTO> create(@RequestBody @Valid EnfermedadCronicaDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnfermedadCronicaDTO> update(@PathVariable Integer id,
            @RequestBody @Valid EnfermedadCronicaDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
