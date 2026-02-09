package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tripletas_neutrosoficas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TripletaNeutrosofica {

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

    @Column(name = "T_verdad", nullable = false, precision = 3, scale = 2)
    private BigDecimal tVerdad;

    @Column(name = "I_indeterminacion", nullable = false, precision = 3, scale = 2)
    private BigDecimal iIndeterminacion;

    @Column(name = "F_falsedad", nullable = false, precision = 3, scale = 2)
    private BigDecimal fFalsedad;

    @Column(name = "peso_ponderado", nullable = false, precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal pesoPonderado = BigDecimal.ONE;
}
