package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.ParametroSistema;
import com.application.webapi.repository.ParametroSistemaRepository;
import com.application.webapi.service.ParametroSistemaService;
import com.application.webapi.service.dto.ParametroSistemaDTO;
import com.application.webapi.service.mapper.ParametroSistemaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ParametroSistemaServiceImpl implements ParametroSistemaService {

    private final ParametroSistemaRepository repository;
    private final ParametroSistemaMapper mapper;

    @Override
    public List<ParametroSistemaDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ParametroSistemaDTO> findById(UUID id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    public Optional<ParametroSistemaDTO> findByClave(String clave) {
        return repository.findByClave(clave).map(mapper::toDto);
    }

    @Override
    public ParametroSistemaDTO create(ParametroSistemaDTO dto) {
        if (repository.findByClave(dto.getClave()).isPresent()) {
            throw new IllegalArgumentException("Ya existe un parámetro con la clave: " + dto.getClave());
        }
        ParametroSistema entity = mapper.toEntity(dto);
        // Ensure new parameters are editable by default unless specified
        // entity.setEditable(true); // Default in entity builder
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public ParametroSistemaDTO update(UUID id, ParametroSistemaDTO dto) {
        ParametroSistema entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parámetro no encontrado"));

        if (!entity.isEditable()) {
            throw new IllegalStateException("Este parámetro no es editable");
        }

        entity.setValor(dto.getValor());
        entity.setDescripcion(dto.getDescripcion());
        entity.setFechaModificacion(LocalDateTime.now());
        // TODO: Set modificadoPor from SecurityContext if available

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    @Override
    public void delete(UUID id) {
        ParametroSistema entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Parámetro no encontrado"));

        if (!entity.isEditable()) { // Usually non-editable params shouldn't be deleted either
            throw new IllegalStateException("No se puede eliminar un parámetro del sistema protegido");
        }

        repository.deleteById(id);
    }
}
