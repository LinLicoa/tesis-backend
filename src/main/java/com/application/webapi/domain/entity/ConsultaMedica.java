package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "consultas_medicas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_hora", nullable = false)
    @Builder.Default
    private LocalDateTime fechaHora = LocalDateTime.now();

    // Signos Vitales (Triaje)
    @Column(name = "presion_arterial", length = 20)
    private String presionArterial;

    @Column(name = "frecuencia_cardiaca")
    private Integer frecuenciaCardiaca;

    @Column(precision = 4, scale = 2)
    private BigDecimal temperatura;

    @Column(name = "saturacion_oxigeno")
    private Integer saturacionOxigeno;

    @Column(name = "peso_kg", precision = 5, scale = 2)
    private BigDecimal pesoKg;

    @Column(name = "talla_cm")
    private Integer tallaCm;

    // Evolución Clínica
    @Column(name = "motivo_consulta", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String motivoConsulta;

    @Column(name = "examen_fisico", columnDefinition = "NVARCHAR(MAX)")
    private String examenFisico;

    @Column(name = "diagnostico_cie10", length = 20)
    private String diagnosticoCie10;

    @Column(name = "diagnostico_descripcion", columnDefinition = "NVARCHAR(MAX)")
    private String diagnosticoDescripcion;

    @Column(name = "plan_tratamiento", columnDefinition = "NVARCHAR(MAX)")
    private String planTratamiento;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
