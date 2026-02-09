package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.EnfermedadCronica;
import com.application.webapi.repository.EnfermedadCronicaRepository;
import com.application.webapi.service.EnfermedadCronicaService;
import com.application.webapi.service.dto.EnfermedadCronicaDTO;
import com.application.webapi.service.mapper.EnfermedadCronicaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EnfermedadCronicaServiceImpl implements EnfermedadCronicaService {

    private final EnfermedadCronicaRepository repository;
    private final EnfermedadCronicaMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<EnfermedadCronicaDTO> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnfermedadCronicaDTO> findById(Integer id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public EnfermedadCronicaDTO save(EnfermedadCronicaDTO dto) {
        if (repository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una enfermedad crónica con el nombre: " + dto.getNombre());
        }
        EnfermedadCronica entity = mapper.toEntity(dto);
        // Ensure ID is null for creation to trigger autoincrement
        entity.setId(null);
        return mapper.toDto(repository.save(entity));
    }

    @Override
    public EnfermedadCronicaDTO update(Integer id, EnfermedadCronicaDTO dto) {
        EnfermedadCronica existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enfermedad crónica no encontrada con ID: " + id));

        if (!existing.getNombre().equalsIgnoreCase(dto.getNombre()) && repository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("Ya existe una enfermedad crónica con el nombre: " + dto.getNombre());
        }

        existing.setNombre(dto.getNombre());
        return mapper.toDto(repository.save(existing));
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Enfermedad crónica no encontrada con ID: " + id);
        }
        repository.deleteById(id);
    }
}
