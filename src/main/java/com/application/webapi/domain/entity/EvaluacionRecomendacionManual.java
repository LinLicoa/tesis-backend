package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evaluaciones_recomendaciones_manuales")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluacionRecomendacionManual {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @Column(name = "recomendacion_texto", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String recomendacionTexto;

    @CreationTimestamp
    @Column(name = "fecha_asignacion", updatable = false)
    private LocalDateTime fechaAsignacion;
}
