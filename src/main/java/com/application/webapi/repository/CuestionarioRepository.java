package com.application.webapi.repository;

import com.application.webapi.domain.entity.Cuestionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CuestionarioRepository extends JpaRepository<Cuestionario, Integer> {

    List<Cuestionario> findByActivoTrueOrderByTipoAscNumeroItemAsc();

    List<Cuestionario> findByTipoAndActivoTrueOrderByNumeroItemAsc(String tipo);
}
