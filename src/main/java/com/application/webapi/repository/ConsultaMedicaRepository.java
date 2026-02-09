package com.application.webapi.repository;

import com.application.webapi.domain.entity.ConsultaMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConsultaMedicaRepository extends JpaRepository<ConsultaMedica, UUID> {

    List<ConsultaMedica> findByPacienteIdOrderByFechaHoraDesc(UUID pacienteId);

    List<ConsultaMedica> findByUsuarioIdOrderByFechaHoraDesc(UUID usuarioId);

    List<ConsultaMedica> findByPacienteId(UUID pacienteId);
}
