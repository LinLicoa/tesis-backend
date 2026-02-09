package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tokens_recuperacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TokenRecuperacion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 500, unique = true)
    private String token;

    @Column(name = "fecha_generacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaGeneracion = LocalDateTime.now();

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(nullable = false)
    @Builder.Default
    private boolean usado = false;

    @Column(name = "fecha_uso")
    private LocalDateTime fechaUso;
}
