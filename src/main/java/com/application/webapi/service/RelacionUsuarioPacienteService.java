package com.application.webapi.service;

import com.application.webapi.service.dto.RelacionUsuarioPacienteDTO;

import java.util.List;
import java.util.UUID;

public interface RelacionUsuarioPacienteService {

    RelacionUsuarioPacienteDTO create(UUID usuarioId, UUID pacienteId);

    List<RelacionUsuarioPacienteDTO> findByUsuarioId(UUID usuarioId);

    List<RelacionUsuarioPacienteDTO> findByPacienteId(UUID pacienteId);

    void deactivate(Integer id);

    void deactivateByUsuarioAndPaciente(UUID usuarioId, UUID pacienteId);

    boolean existsActiveRelation(UUID usuarioId, UUID pacienteId);
}
