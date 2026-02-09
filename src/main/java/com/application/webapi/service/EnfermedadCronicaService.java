package com.application.webapi.service;

import com.application.webapi.service.dto.EnfermedadCronicaDTO;
import java.util.List;
import java.util.Optional;

public interface EnfermedadCronicaService {
    List<EnfermedadCronicaDTO> findAll();

    Optional<EnfermedadCronicaDTO> findById(Integer id);

    EnfermedadCronicaDTO save(EnfermedadCronicaDTO dto);

    EnfermedadCronicaDTO update(Integer id, EnfermedadCronicaDTO dto);

    void delete(Integer id);
}
