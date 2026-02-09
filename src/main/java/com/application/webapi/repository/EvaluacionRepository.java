package com.application.webapi.repository;

import com.application.webapi.domain.entity.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, UUID> {
    List<Evaluacion> findByPacienteId(UUID pacienteId);

    List<Evaluacion> findByUsuarioId(UUID usuarioId);
}
