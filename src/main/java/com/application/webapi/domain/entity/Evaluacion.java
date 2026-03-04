package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "evaluaciones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consulta_id")
    private ConsultaMedica consulta;

    @Column(name = "fecha_hora", nullable = false)
    @Builder.Default
    private LocalDateTime fechaHora = LocalDateTime.now();

    @Column(nullable = false, length = 20)
    private String estado;

    // Puntajes calculados
    @Column(name = "gad7_puntaje")
    private Integer gad7Puntaje;

    @Column(name = "phq9_puntaje")
    private Integer phq9Puntaje;

    @Column(name = "pss10_puntaje")
    private Integer pss10Puntaje;

    // Clasificaciones
    @Column(name = "nivel_ansiedad", length = 20)
    private String nivelAnsiedad;

    @Column(name = "nivel_depresion", length = 20)
    private String nivelDepresion;

    @Column(name = "nivel_estres", length = 20)
    private String nivelEstres;

    // Tripletas globales - Ansiedad
    @Column(name = "ansiedad_T", precision = 20, scale = 18)
    private BigDecimal ansiedadT;
    @Column(name = "ansiedad_I", precision = 20, scale = 18)
    private BigDecimal ansiedadI;
    @Column(name = "ansiedad_F", precision = 20, scale = 18)
    private BigDecimal ansiedadF;
    @Column(name = "ansiedad_T_bruto", precision = 20, scale = 18)
    private BigDecimal ansiedadTBruto;
    @Column(name = "ansiedad_I_bruto", precision = 20, scale = 18)
    private BigDecimal ansiedadIBruto;
    @Column(name = "ansiedad_F_bruto", precision = 20, scale = 18)
    private BigDecimal ansiedadFBruto;

    // Tripletas globales - Depresión
    @Column(name = "depresion_T", precision = 20, scale = 18)
    private BigDecimal depresionT;
    @Column(name = "depresion_I", precision = 20, scale = 18)
    private BigDecimal depresionI;
    @Column(name = "depresion_F", precision = 20, scale = 18)
    private BigDecimal depresionF;
    @Column(name = "depresion_T_bruto", precision = 20, scale = 18)
    private BigDecimal depresionTBruto;
    @Column(name = "depresion_I_bruto", precision = 20, scale = 18)
    private BigDecimal depresionIBruto;
    @Column(name = "depresion_F_bruto", precision = 20, scale = 18)
    private BigDecimal depresionFBruto;

    // Tripletas globales - Estrés
    @Column(name = "estres_T", precision = 20, scale = 18)
    private BigDecimal estresT;
    @Column(name = "estres_I", precision = 20, scale = 18)
    private BigDecimal estresI;
    @Column(name = "estres_F", precision = 20, scale = 18)
    private BigDecimal estresF;
    @Column(name = "estres_T_bruto", precision = 20, scale = 18)
    private BigDecimal estresTBruto;
    @Column(name = "estres_I_bruto", precision = 20, scale = 18)
    private BigDecimal estresIBruto;
    @Column(name = "estres_F_bruto", precision = 20, scale = 18)
    private BigDecimal estresFBruto;

    // Probabilidades
    @Column(name = "prob_adherencia_alta", precision = 5, scale = 2)
    private BigDecimal porcentajeAnsiedad;
    @Column(name = "prob_adherencia_media", precision = 5, scale = 2)
    private BigDecimal porcentajeDepresion;
    @Column(name = "prob_adherencia_baja", precision = 5, scale = 2)
    private BigDecimal porcentajeEstres;

    @Column(name = "tiempo_procesamiento")
    private Integer tiempoProcesamiento;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String observaciones;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
