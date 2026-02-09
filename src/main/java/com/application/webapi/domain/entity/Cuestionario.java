package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cuestionarios", uniqueConstraints = {
        @UniqueConstraint(name = "UQ_cuestionario_item", columnNames = { "tipo", "numero_item" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cuestionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 10)
    private String tipo;

    @Column(name = "numero_item", nullable = false)
    private int numeroItem;

    @Column(name = "texto_pregunta", nullable = false, length = 500)
    private String textoPregunta;

    @Column(name = "es_inversa", nullable = false)
    @Builder.Default
    private boolean esInversa = false;

    @Column(name = "es_critica", nullable = false)
    @Builder.Default
    private boolean esCritica = false;

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;
}
