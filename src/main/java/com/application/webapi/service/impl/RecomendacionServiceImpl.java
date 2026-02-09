package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.Recomendacion;
import com.application.webapi.repository.RecomendacionRepository;
import com.application.webapi.service.RecomendacionService;
import com.application.webapi.service.dto.RecomendacionDTO;
import com.application.webapi.service.mapper.RecomendacionMapper;
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
public class RecomendacionServiceImpl implements RecomendacionService {

    private final RecomendacionRepository recomendacionRepository;
    private final RecomendacionMapper recomendacionMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionDTO> findAll() {
        return recomendacionRepository.findAll().stream()
                .map(recomendacionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionDTO> findByDimension(String dimension) {
        return recomendacionRepository.findByDimensionAplicableAndActivoTrue(dimension).stream()
                .map(recomendacionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecomendacionDTO> findById(UUID id) {
        return recomendacionRepository.findById(id)
                .map(recomendacionMapper::toDto);
    }

    @Override
    public RecomendacionDTO create(RecomendacionDTO recomendacionDTO) {
        Recomendacion entity = recomendacionMapper.toEntity(recomendacionDTO);
        entity = recomendacionRepository.save(entity);
        return recomendacionMapper.toDto(entity);
    }
}
