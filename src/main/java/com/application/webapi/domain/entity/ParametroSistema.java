package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "parametros_sistema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParametroSistema {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100, unique = true)
    private String clave;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String valor;

    @Column(name = "tipo_dato", nullable = false, length = 20)
    private String tipoDato;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    @Builder.Default
    private boolean editable = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modificado_por")
    private Usuario modificadoPor;

    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
