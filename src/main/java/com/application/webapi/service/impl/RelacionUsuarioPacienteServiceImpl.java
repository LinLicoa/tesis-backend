package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.Paciente;
import com.application.webapi.domain.entity.RelacionUsuarioPaciente;
import com.application.webapi.domain.entity.Usuario;
import com.application.webapi.repository.PacienteRepository;
import com.application.webapi.repository.RelacionUsuarioPacienteRepository;
import com.application.webapi.repository.UsuarioRepository;
import com.application.webapi.service.RelacionUsuarioPacienteService;
import com.application.webapi.service.dto.RelacionUsuarioPacienteDTO;
import com.application.webapi.service.mapper.RelacionUsuarioPacienteMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RelacionUsuarioPacienteServiceImpl implements RelacionUsuarioPacienteService {

    private final RelacionUsuarioPacienteRepository relacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final RelacionUsuarioPacienteMapper mapper;

    @Override
    public RelacionUsuarioPacienteDTO create(UUID usuarioId, UUID pacienteId) {
        // Check if relation already exists
        if (relacionRepository.existsByUsuarioIdAndPacienteIdAndActivoTrue(usuarioId, pacienteId)) {
            throw new IllegalStateException("La relación entre el usuario y el paciente ya existe");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        RelacionUsuarioPaciente relacion = RelacionUsuarioPaciente.builder()
                .usuario(usuario)
                .paciente(paciente)
                .build();

        relacion = relacionRepository.save(relacion);
        return mapper.toDto(relacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelacionUsuarioPacienteDTO> findByUsuarioId(UUID usuarioId) {
        return relacionRepository.findByUsuarioIdAndActivoTrue(usuarioId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelacionUsuarioPacienteDTO> findByPacienteId(UUID pacienteId) {
        return relacionRepository.findByPacienteIdAndActivoTrue(pacienteId)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deactivate(Integer id) {
        RelacionUsuarioPaciente relacion = relacionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Relación no encontrada"));
        relacion.setActivo(false);
        relacionRepository.save(relacion);
    }

    @Override
    public void deactivateByUsuarioAndPaciente(UUID usuarioId, UUID pacienteId) {
        RelacionUsuarioPaciente relacion = relacionRepository
                .findByUsuarioIdAndPacienteIdAndActivoTrue(usuarioId, pacienteId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Relación activa no encontrada entre el usuario y el paciente"));
        relacion.setActivo(false);
        relacionRepository.save(relacion);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsActiveRelation(UUID usuarioId, UUID pacienteId) {
        return relacionRepository.existsByUsuarioIdAndPacienteIdAndActivoTrue(usuarioId, pacienteId);
    }
}
