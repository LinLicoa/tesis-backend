package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sesiones")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 500, unique = true)
    private String token;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    @Column(name = "fecha_inicio", nullable = false)
    @Builder.Default
    private LocalDateTime fechaInicio = LocalDateTime.now();

    @Column(name = "fecha_expiracion", nullable = false)
    private LocalDateTime fechaExpiracion;

    @Column(name = "recordar_sesion", nullable = false)
    private boolean recordarSesion;

    @Column(nullable = false)
    @Builder.Default
    private boolean activa = true;
}
