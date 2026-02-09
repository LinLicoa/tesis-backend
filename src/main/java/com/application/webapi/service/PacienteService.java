package com.application.webapi.service;

import com.application.webapi.service.dto.PacienteDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PacienteService {
    PacienteDTO create(PacienteDTO pacienteDTO, UUID usuarioId);

    List<PacienteDTO> findAll();

    Optional<PacienteDTO> findById(UUID id);

    List<PacienteDTO> findByUsuarioId(UUID usuarioId);

    Optional<PacienteDTO> findByCedula(String cedula);

    void delete(UUID id);

    PacienteDTO update(UUID id, PacienteDTO dto);
}
