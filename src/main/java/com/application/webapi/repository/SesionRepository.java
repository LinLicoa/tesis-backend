package com.application.webapi.repository;

import com.application.webapi.domain.entity.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, UUID> {
    Optional<Sesion> findByToken(String token);

    void deleteByUsuarioId(UUID usuarioId);
}
