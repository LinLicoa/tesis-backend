package com.application.webapi.service;

import com.application.webapi.service.dto.CuestionarioAgrupadoDTO;

import java.util.List;

public interface CuestionarioService {

    List<CuestionarioAgrupadoDTO> findAllGroupedByTipo();
}
