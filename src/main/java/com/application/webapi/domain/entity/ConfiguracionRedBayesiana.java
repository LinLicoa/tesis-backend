package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "configuracion_red_bayesiana", uniqueConstraints = {
        @UniqueConstraint(name = "UQ_nodo_version", columnNames = { "nodo", "version" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfiguracionRedBayesiana {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nodo;

    @Column(name = "configuracion_json", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String configuracionJson;

    @Column(nullable = false)
    private int version;

    @Column(name = "fecha_actualizacion", nullable = false)
    @Builder.Default
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String comentarios;
}
