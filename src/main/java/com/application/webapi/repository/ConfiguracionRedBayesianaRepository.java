package com.application.webapi.repository;

import com.application.webapi.domain.entity.ConfiguracionRedBayesiana;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ConfiguracionRedBayesianaRepository extends JpaRepository<ConfiguracionRedBayesiana, UUID> {
    // Methods can be added as needed
}
