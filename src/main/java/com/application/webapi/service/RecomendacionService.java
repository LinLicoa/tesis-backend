package com.application.webapi.service;

import com.application.webapi.service.dto.RecomendacionDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RecomendacionService {
    List<RecomendacionDTO> findAll();

    List<RecomendacionDTO> findByDimension(String dimension);

    Optional<RecomendacionDTO> findById(UUID id);

    RecomendacionDTO create(RecomendacionDTO recomendacionDTO);
}
