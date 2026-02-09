package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.ConsultaMedica;
import com.application.webapi.domain.entity.Paciente;
import com.application.webapi.domain.entity.Usuario;
import com.application.webapi.repository.ConsultaMedicaRepository;
import com.application.webapi.repository.PacienteRepository;
import com.application.webapi.repository.UsuarioRepository;
import com.application.webapi.service.ConsultaMedicaService;
import com.application.webapi.service.dto.ConsultaMedicaDTO;
import com.application.webapi.service.mapper.ConsultaMedicaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ConsultaMedicaServiceImpl implements ConsultaMedicaService {

    private final ConsultaMedicaRepository consultaMedicaRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConsultaMedicaMapper consultaMedicaMapper;

    @Override
    public ConsultaMedicaDTO create(ConsultaMedicaDTO dto, UUID pacienteId, UUID usuarioId) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ConsultaMedica entity = consultaMedicaMapper.toEntity(dto);
        entity.setPaciente(paciente);
        entity.setUsuario(usuario);

        ConsultaMedica saved = consultaMedicaRepository.save(entity);
        return consultaMedicaMapper.toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConsultaMedicaDTO> findById(UUID id) {
        return consultaMedicaRepository.findById(id)
                .map(consultaMedicaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaMedicaDTO> findByPacienteId(UUID pacienteId) {
        return consultaMedicaRepository.findByPacienteIdOrderByFechaHoraDesc(pacienteId)
                .stream()
                .map(consultaMedicaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConsultaMedicaDTO> findByUsuarioId(UUID usuarioId) {
        return consultaMedicaRepository.findByUsuarioIdOrderByFechaHoraDesc(usuarioId)
                .stream()
                .map(consultaMedicaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ConsultaMedicaDTO update(UUID id, ConsultaMedicaDTO dto) {
        ConsultaMedica entity = consultaMedicaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta médica no encontrada"));

        consultaMedicaMapper.updateEntityFromDto(dto, entity);
        ConsultaMedica updated = consultaMedicaRepository.save(entity);
        return consultaMedicaMapper.toDto(updated);
    }

    @Override
    public void delete(UUID id) {
        if (!consultaMedicaRepository.existsById(id)) {
            throw new RuntimeException("Consulta médica no encontrada");
        }
        consultaMedicaRepository.deleteById(id);
    }
}
