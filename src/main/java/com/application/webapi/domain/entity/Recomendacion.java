package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "recomendaciones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recomendacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String descripcion;

    @Column(nullable = false, length = 30)
    private String categoria;

    @Column(name = "dimension_aplicable", nullable = false, length = 20)
    private String dimensionAplicable;

    @Column(name = "nivel_minimo", length = 20)
    private String nivelMinimo;

    @Column(name = "nivel_maximo", length = 20)
    private String nivelMaximo;

    @Column(nullable = false)
    @Builder.Default
    private int prioridad = 1;

    @Column(name = "es_urgente", nullable = false)
    @Builder.Default
    private boolean esUrgente = false;

    @Column(name = "evidencia_cientifica", columnDefinition = "NVARCHAR(MAX)")
    private String evidenciaCientifica;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
