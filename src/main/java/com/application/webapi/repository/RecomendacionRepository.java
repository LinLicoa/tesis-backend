package com.application.webapi.repository;

import com.application.webapi.domain.entity.Recomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RecomendacionRepository extends JpaRepository<Recomendacion, UUID> {
    List<Recomendacion> findByDimensionAplicableAndActivoTrue(String dimensionAplicable);

    List<Recomendacion> findByCategoriaAndActivoTrue(String categoria);

    List<Recomendacion> findByDescripcionContainingIgnoreCase(String descripcion);
}
