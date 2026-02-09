package com.application.webapi.repository;

import com.application.webapi.domain.entity.ParametroSistema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ParametroSistemaRepository extends JpaRepository<ParametroSistema, UUID> {
    Optional<ParametroSistema> findByClave(String clave);
}
