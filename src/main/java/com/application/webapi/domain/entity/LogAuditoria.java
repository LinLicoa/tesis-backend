package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "logs_auditoria")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "rol_usuario", length = 20)
    private String rolUsuario;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(nullable = false, length = 50)
    private String entidad;

    @Column(name = "entidad_id")
    private UUID entidadId;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String detalles;

    @Column(nullable = false, length = 20)
    private String resultado;

    @Column(name = "fecha_hora", nullable = false)
    @Builder.Default
    private LocalDateTime fechaHora = LocalDateTime.now();
}
