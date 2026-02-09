package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "respuestas_cuestionarios")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespuestaCuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_id", nullable = false)
    private Evaluacion evaluacion;

    @Column(nullable = false, length = 10)
    private String cuestionario;

    @Column(name = "numero_item", nullable = false)
    private int numeroItem;

    @Column(nullable = false)
    private int respuesta;

    @Column(name = "fecha_respuesta", nullable = false)
    @Builder.Default
    private LocalDateTime fechaRespuesta = LocalDateTime.now();
}
