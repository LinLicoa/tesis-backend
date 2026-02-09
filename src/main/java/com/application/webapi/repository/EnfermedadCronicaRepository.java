package com.application.webapi.repository;

import com.application.webapi.domain.entity.EnfermedadCronica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnfermedadCronicaRepository extends JpaRepository<EnfermedadCronica, Integer> {
    Optional<EnfermedadCronica> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
