package com.application.webapi.controller;

import com.application.webapi.service.RecomendacionService;
import com.application.webapi.service.dto.RecomendacionDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/recomendaciones")
@RequiredArgsConstructor
public class RecomendacionController {

    private final RecomendacionService recomendacionService;

    @GetMapping
    public ResponseEntity<List<RecomendacionDTO>> getAll() {
        return ResponseEntity.ok(recomendacionService.findAll());
    }

    @GetMapping("/dimension/{dimension}")
    public ResponseEntity<List<RecomendacionDTO>> getByDimension(@PathVariable String dimension) {
        return ResponseEntity.ok(recomendacionService.findByDimension(dimension));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecomendacionDTO> getById(@PathVariable UUID id) {
        return recomendacionService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RecomendacionDTO> create(@Valid @RequestBody RecomendacionDTO recomendacionDTO) {
        return ResponseEntity.ok(recomendacionService.create(recomendacionDTO));
    }
}
