package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.Cuestionario;
import com.application.webapi.repository.CuestionarioRepository;
import com.application.webapi.service.CuestionarioService;
import com.application.webapi.service.dto.CuestionarioAgrupadoDTO;
import com.application.webapi.service.dto.PreguntaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CuestionarioServiceImpl implements CuestionarioService {

    private final CuestionarioRepository cuestionarioRepository;

    @Override
    public List<CuestionarioAgrupadoDTO> findAllGroupedByTipo() {
        List<Cuestionario> cuestionarios = cuestionarioRepository.findByActivoTrueOrderByTipoAscNumeroItemAsc();

        // Group by tipo
        Map<String, List<Cuestionario>> grouped = cuestionarios.stream()
                .collect(Collectors.groupingBy(Cuestionario::getTipo));

        // Convert to DTOs
        return grouped.entrySet().stream()
                .map(entry -> CuestionarioAgrupadoDTO.builder()
                        .tipo(entry.getKey())
                        .preguntas(entry.getValue().stream()
                                .map(c -> PreguntaDTO.builder()
                                        .id(c.getId())
                                        .texto(c.getTextoPregunta())
                                        .numero(c.getNumeroItem())
                                        .esInversa(c.isEsInversa())
                                        .esCritica(c.isEsCritica())
                                        .build())
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
