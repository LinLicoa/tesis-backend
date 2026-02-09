package com.application.webapi.controller;

import com.application.webapi.service.dto.UsuarioDTO;
import com.application.webapi.service.UsuarioService;
import com.application.webapi.service.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    // Registration is moved to AuthController

    @org.springframework.security.access.prepost.PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/admin")
    public ResponseEntity<UsuarioDTO> createAdmin(@RequestBody @jakarta.validation.Valid UsuarioDTO dto) {
        com.application.webapi.domain.entity.Usuario usuario = usuarioMapper.toEntity(dto);
        usuario = usuarioService.createAdmin(usuario);
        return ResponseEntity.ok(usuarioMapper.toDto(usuario));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getById(@PathVariable UUID id) {
        return usuarioService.findById(id)
                .map(usuarioMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
