package com.application.webapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreguntaDTO {
    private Integer id;
    private String texto;
    private int numero;
    private boolean esInversa;
    private boolean esCritica;
}
