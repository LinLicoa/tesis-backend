package com.application.webapi.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pacientes", uniqueConstraints = {
        @UniqueConstraint(name = "UQ_paciente_cedula", columnNames = { "cedula" })
})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 20, unique = true)
    private String cedula;

    @Column(name = "nombre_encriptado", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String nombreEncriptado;

    @Column(nullable = false)
    private int edad;

    @Column(nullable = false, length = 30)
    private String genero;

    @Column(name = "enfermedad_cronica", nullable = false, length = 150)
    private String enfermedadCronica;

    // Campos clínicos históricos
    @Column(name = "tipo_sangre", length = 5)
    private String tipoSangre;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String alergias;

    @Column(name = "antecedentes_familiares", columnDefinition = "NVARCHAR(MAX)")
    private String antecedentesFamiliares;

    @Column(length = 100)
    private String ocupacion;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private boolean activo = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
