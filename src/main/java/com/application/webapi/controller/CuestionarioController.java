package com.application.webapi.controller;

import com.application.webapi.service.CuestionarioService;
import com.application.webapi.service.dto.CuestionarioAgrupadoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cuestionarios")
@RequiredArgsConstructor
public class CuestionarioController {

    private final CuestionarioService cuestionarioService;

    @GetMapping
    public ResponseEntity<List<CuestionarioAgrupadoDTO>> getAll() {
        return ResponseEntity.ok(cuestionarioService.findAllGroupedByTipo());
    }
}
