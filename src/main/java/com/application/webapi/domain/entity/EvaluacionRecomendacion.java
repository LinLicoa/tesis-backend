package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evaluaciones_recomendaciones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluacionRecomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recomendacion_id", nullable = false)
    private Recomendacion recomendacion;

    @Column(name = "criterio_seleccion", length = 200)
    private String criterioSeleccion;

    @Column(name = "orden_presentacion", nullable = false)
    private int ordenPresentacion;

    @Column(name = "fecha_asignacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaAsignacion = LocalDateTime.now();
}
