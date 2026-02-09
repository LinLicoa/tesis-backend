package com.application.webapi.repository;

import com.application.webapi.domain.entity.TripletaNeutrosofica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TripletaNeutrosoficaRepository extends JpaRepository<TripletaNeutrosofica, UUID> {
    List<TripletaNeutrosofica> findByEvaluacionId(UUID evaluacionId);
}
