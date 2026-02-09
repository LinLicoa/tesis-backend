package com.application.webapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RespuestasEvaluacionDTO {

    private List<RespuestaItemDTO> gad7;
    private List<RespuestaItemDTO> phq9;
    private List<RespuestaItemDTO> pss10;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RespuestaItemDTO {
        private int numeroItem;
        private int respuesta;
    }
}
