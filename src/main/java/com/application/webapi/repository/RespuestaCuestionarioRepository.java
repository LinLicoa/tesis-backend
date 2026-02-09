package com.application.webapi.repository;

import com.application.webapi.domain.entity.RespuestaCuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RespuestaCuestionarioRepository extends JpaRepository<RespuestaCuestionario, UUID> {
    List<RespuestaCuestionario> findByEvaluacionId(UUID evaluacionId);
}
