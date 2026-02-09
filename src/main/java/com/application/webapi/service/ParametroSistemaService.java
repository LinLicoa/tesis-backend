package com.application.webapi.service;

import com.application.webapi.service.dto.ParametroSistemaDTO;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ParametroSistemaService {
    List<ParametroSistemaDTO> findAll();

    Optional<ParametroSistemaDTO> findById(UUID id);

    Optional<ParametroSistemaDTO> findByClave(String clave);

    ParametroSistemaDTO create(ParametroSistemaDTO dto);

    ParametroSistemaDTO update(UUID id, ParametroSistemaDTO dto);

    void delete(UUID id);
}
