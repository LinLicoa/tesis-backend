package com.application.webapi.repository;

import com.application.webapi.domain.entity.EvaluacionRecomendacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface EvaluacionRecomendacionRepository extends JpaRepository<EvaluacionRecomendacion, UUID> {
    List<EvaluacionRecomendacion> findByEvaluacionIdOrderByOrdenPresentacionAsc(UUID evaluacionId);
}
