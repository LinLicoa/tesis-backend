package com.application.webapi.service;

import com.application.webapi.service.dto.ConsultaMedicaDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ConsultaMedicaService {

    ConsultaMedicaDTO create(ConsultaMedicaDTO dto, UUID pacienteId, UUID usuarioId);

    Optional<ConsultaMedicaDTO> findById(UUID id);

    List<ConsultaMedicaDTO> findByPacienteId(UUID pacienteId);

    List<ConsultaMedicaDTO> findByUsuarioId(UUID usuarioId);

    ConsultaMedicaDTO update(UUID id, ConsultaMedicaDTO dto);

    void delete(UUID id);
}
