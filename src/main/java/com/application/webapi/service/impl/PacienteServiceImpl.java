package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.Paciente;
import com.application.webapi.domain.entity.RelacionUsuarioPaciente;
import com.application.webapi.domain.entity.Usuario;
import com.application.webapi.repository.PacienteRepository;
import com.application.webapi.repository.RelacionUsuarioPacienteRepository;
import com.application.webapi.repository.UsuarioRepository;
import com.application.webapi.service.PacienteService;
import com.application.webapi.service.dto.PacienteDTO;
import com.application.webapi.service.mapper.PacienteMapper;
import jakarta.persistence.EntityNotFoundException;
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
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final RelacionUsuarioPacienteRepository relacionRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    public PacienteDTO create(PacienteDTO pacienteDTO, UUID usuarioId) {
        // Verify usuario exists
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

        // Create paciente
        Paciente entity = pacienteMapper.toEntity(pacienteDTO);
        entity = pacienteRepository.save(entity);

        // Create relationship between usuario and paciente
        RelacionUsuarioPaciente relacion = RelacionUsuarioPaciente.builder()
                .usuario(usuario)
                .paciente(entity)
                .build();
        relacionRepository.save(relacion);

        return pacienteMapper.toDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteDTO> findAll() {
        return pacienteRepository.findAll().stream()
                .map(pacienteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PacienteDTO> findById(UUID id) {
        return pacienteRepository.findById(id)
                .map(pacienteMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteDTO> findByUsuarioId(UUID usuarioId) {
        // Get all relationships for this usuario
        return relacionRepository.findByUsuarioIdAndActivoTrue(usuarioId).stream()
                .map(rel -> pacienteMapper.toDto(rel.getPaciente()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PacienteDTO> findByCedula(String cedula) {
        return pacienteRepository.findByCedula(cedula)
                .map(pacienteMapper::toDto);
    }

    @Override
    public void delete(UUID id) {
        pacienteRepository.deleteById(id);
    }

    @Override
    public PacienteDTO update(UUID id, PacienteDTO dto) {
        Paciente existing = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));

        // Update fields
        if (dto.getCedula() != null)
            existing.setCedula(dto.getCedula());
        if (dto.getNombreEncriptado() != null)
            existing.setNombreEncriptado(dto.getNombreEncriptado());
        if (dto.getEdad() != 0)
            existing.setEdad(dto.getEdad());
        if (dto.getGenero() != null)
            existing.setGenero(dto.getGenero());
        if (dto.getEnfermedadCronica() != null)
            existing.setEnfermedadCronica(dto.getEnfermedadCronica());

        // Campos clínicos históricos
        if (dto.getTipoSangre() != null)
            existing.setTipoSangre(dto.getTipoSangre());
        if (dto.getAlergias() != null)
            existing.setAlergias(dto.getAlergias());
        if (dto.getAntecedentesFamiliares() != null)
            existing.setAntecedentesFamiliares(dto.getAntecedentesFamiliares());
        if (dto.getOcupacion() != null)
            existing.setOcupacion(dto.getOcupacion());

        return pacienteMapper.toDto(pacienteRepository.save(existing));
    }
}
