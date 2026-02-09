package com.application.webapi.repository;

import com.application.webapi.domain.entity.RelacionUsuarioPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RelacionUsuarioPacienteRepository extends JpaRepository<RelacionUsuarioPaciente, Integer> {

    List<RelacionUsuarioPaciente> findByUsuarioIdAndActivoTrue(UUID usuarioId);

    List<RelacionUsuarioPaciente> findByPacienteIdAndActivoTrue(UUID pacienteId);

    Optional<RelacionUsuarioPaciente> findByUsuarioIdAndPacienteId(UUID usuarioId, UUID pacienteId);

    Optional<RelacionUsuarioPaciente> findByUsuarioIdAndPacienteIdAndActivoTrue(UUID usuarioId, UUID pacienteId);

    boolean existsByUsuarioIdAndPacienteIdAndActivoTrue(UUID usuarioId, UUID pacienteId);
}
