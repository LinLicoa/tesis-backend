package com.application.webapi.service.impl;

import com.application.webapi.client.PythonPredictionRequestItem;
import com.application.webapi.client.PythonPredictionResponseItem;
import com.application.webapi.client.PythonServiceClient;
import com.application.webapi.domain.entity.*;
import com.application.webapi.repository.*;
import com.application.webapi.service.ProcesamientoAsyncService;
import com.application.webapi.service.dto.ResultadosEvaluacionDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcesamientoAsyncServiceImpl implements ProcesamientoAsyncService {

    private final EvaluacionRepository evaluacionRepo;
    private final RespuestaCuestionarioRepository respuestaRepo;
    private final EvaluacionRecomendacionRepository evalRecomendacionRepo;
    private final EvaluacionRecomendacionManualRepository evalRecomendacionManualRepo;
    private final RecomendacionRepository recomendacionRepo;
    private final PythonServiceClient pythonClient;
    private final org.springframework.transaction.PlatformTransactionManager transactionManager;

    private static final List<Integer> PSS10_ITEMS_INVERSOS = Arrays.asList(4, 5, 7, 8);

    @Override
    @Async("evaluacionExecutor")
    @Transactional
    public CompletableFuture<ResultadosEvaluacionDTO> procesarEvaluacionAsync(UUID evaluacionId) {
        long tiempoInicio = System.currentTimeMillis();

        try {
            log.info("Iniciando procesamiento asíncrono de evaluación: {}", evaluacionId);

            // PASO 1: Obtener datos de la evaluación
            Evaluacion evaluacion = evaluacionRepo.findById(evaluacionId)
                    .orElseThrow(() -> new EntityNotFoundException("Evaluación no encontrada"));

            List<RespuestaCuestionario> respuestas = respuestaRepo.findByEvaluacionId(evaluacionId);

            // PASO 2: Calcular puntajes
            Map<String, Integer> puntajes = calcularPuntajes(respuestas);
            evaluacion.setGad7Puntaje(puntajes.get("GAD7"));
            evaluacion.setPhq9Puntaje(puntajes.get("PHQ9"));
            evaluacion.setPss10Puntaje(puntajes.get("PSS10"));

            // PASO 3: Clasificar niveles
            evaluacion.setNivelAnsiedad(clasificarAnsiedad(puntajes.get("GAD7")));
            evaluacion.setNivelDepresion(clasificarDepresion(puntajes.get("PHQ9")));
            evaluacion.setNivelEstres(clasificarEstres(puntajes.get("PSS10")));
            evaluacionRepo.save(evaluacion);

            // PASO 4: Preparar request para Python (Nueva estructura lista)
            List<PythonPredictionRequestItem> pythonRequest = new ArrayList<>();
            // Usamos 1 como ID fijo ya que procesamos una por una
            int idCuestionarioFake = 1;

            for (RespuestaCuestionario r : respuestas) {
                // Mapear nombre cuestionario: GAD7 -> GAD-7
                String codigoPrueba = formatCodigoPrueba(r.getCuestionario());

                // Calculate global question ID (1-26) based on test type
                int globalPreguntaId = r.getNumeroItem();
                if ("PHQ9".equals(r.getCuestionario())) {
                    globalPreguntaId += 7;
                } else if ("PSS10".equals(r.getCuestionario())) {
                    globalPreguntaId += 16;
                }

                pythonRequest.add(PythonPredictionRequestItem.builder()
                        .idCuestionario(idCuestionarioFake)
                        .pregunta(globalPreguntaId)
                        .valor(r.getRespuesta())
                        .codigoPrueba(codigoPrueba)
                        .build());
            }

            // PASO 5: Llamar a Python (endpoint /predecir)
            PythonPredictionResponseItem[] responseArray = pythonClient.post(
                    "/predecir", pythonRequest, PythonPredictionResponseItem[].class);

            if (responseArray == null || responseArray.length == 0) {
                throw new RuntimeException("Respuesta vacía del servicio Python");
            }

            PythonPredictionResponseItem pythonResponse = responseArray[0];

            // PASO 6: Guardar resultados de Python
            if (pythonResponse.getDetalles() != null) {
                log.info("Respuesta Python recibida. Detalles: {}", pythonResponse.getDetalles().size());

                // Mapear detalles a tripletas globales (Directas)
                for (PythonPredictionResponseItem.DetalleDTO detalle : pythonResponse.getDetalles()) {
                    BigDecimal tVal = BigDecimal.valueOf(detalle.getT());
                    BigDecimal iVal = BigDecimal.valueOf(detalle.getI());
                    BigDecimal fVal = BigDecimal.valueOf(detalle.getF());

                    // Map Percentage to Probabilities (Mapping Logic: GAD7->Alta, PHQ9->Media,
                    // PSS10->Baja)
                    BigDecimal pctVal = BigDecimal.valueOf(detalle.getPorcentaje());

                    if ("GAD-7".equalsIgnoreCase(detalle.getPrueba())) {
                        evaluacion.setAnsiedadT(tVal);
                        evaluacion.setAnsiedadI(iVal);
                        evaluacion.setAnsiedadF(fVal);
                        evaluacion.setPorcentajeAnsiedad(pctVal);
                    } else if ("PHQ-9".equalsIgnoreCase(detalle.getPrueba())) {
                        evaluacion.setDepresionT(tVal);
                        evaluacion.setDepresionI(iVal);
                        evaluacion.setDepresionF(fVal);
                        evaluacion.setPorcentajeDepresion(pctVal);
                    } else if ("PSS-10".equalsIgnoreCase(detalle.getPrueba())) {
                        evaluacion.setEstresT(tVal);
                        evaluacion.setEstresI(iVal);
                        evaluacion.setEstresF(fVal);
                        evaluacion.setPorcentajeEstres(pctVal);
                    }
                }
            }

            // Asignar recomendaciones
            List<EvaluacionRecomendacion> recomendacionesAsignadas = new ArrayList<>();
            List<String> recomendacionesDirectas = new ArrayList<>();

            if (pythonResponse.getRecomendaciones() != null) {
                // Save recommendations to new table
                for (String textoRecomendacion : pythonResponse.getRecomendaciones()) {
                    try {
                        EvaluacionRecomendacionManual manualRec = EvaluacionRecomendacionManual.builder()
                                .evaluacion(evaluacion)
                                .recomendacionTexto(textoRecomendacion)
                                .build();
                        evalRecomendacionManualRepo.save(manualRec);

                        // Add to direct response list
                        recomendacionesDirectas.add(textoRecomendacion);
                    } catch (Exception e) {
                        log.error("Error saving manual recommendation: {}", e.getMessage());
                    }
                }
            }

            evaluacionRepo.save(evaluacion);

            // PASO 7: Detectar alertas críticas
            boolean tieneIdeacionSuicida = respuestas.stream()
                    .anyMatch(r -> "PHQ9".equals(r.getCuestionario())
                            && r.getNumeroItem() == 9
                            && r.getRespuesta() > 0);

            if (tieneIdeacionSuicida) {
                String alerta = "ALERTA CRÍTICA: Ideación suicida detectada. Requiere evaluación psiquiátrica urgente.";
                if (evaluacion.getObservaciones() == null || !evaluacion.getObservaciones().contains(alerta)) {
                    evaluacion.setObservaciones(alerta + "\n"
                            + (evaluacion.getObservaciones() != null ? evaluacion.getObservaciones() : ""));
                }
            }

            // PASO 8: Finalizar
            long tiempoTotal = System.currentTimeMillis() - tiempoInicio;
            evaluacion.setTiempoProcesamiento((int) tiempoTotal);
            evaluacion.setEstado("completada");
            evaluacionRepo.save(evaluacion);

            log.info("Evaluación {} completada en {} ms", evaluacionId, tiempoTotal);

            // Reemplazamos la lógica anterior de DTO de recomendaciones con la lista
            // combinada
            // List<ResultadosEvaluacionDTO.RecomendacionResultadoDTO> recomendacionesDTO =
            // recomendacionesAsignadas
            // .stream()
            // .map(er -> ResultadosEvaluacionDTO.RecomendacionResultadoDTO.builder()
            // .id(er.getRecomendacion().getId())
            // .titulo(er.getRecomendacion().getTitulo())
            // .descripcion(er.getRecomendacion().getDescripcion())
            // .categoria(er.getRecomendacion().getCategoria())
            // .dimensionAplicable(er.getRecomendacion().getDimensionAplicable())
            // .prioridad(er.getRecomendacion().getPrioridad())
            // .criterioSeleccion(er.getCriterioSeleccion())
            // .build())
            // .toList();

            Map<String, ResultadosEvaluacionDTO.TripletaGlobalDTO> tripletasGlobales = new HashMap<>();
            tripletasGlobales.put("ansiedad", ResultadosEvaluacionDTO.TripletaGlobalDTO.builder()
                    .T(evaluacion.getAnsiedadT() != null ? evaluacion.getAnsiedadT() : BigDecimal.ZERO)
                    .I(evaluacion.getAnsiedadI() != null ? evaluacion.getAnsiedadI() : BigDecimal.ZERO)
                    .F(evaluacion.getAnsiedadF() != null ? evaluacion.getAnsiedadF() : BigDecimal.ZERO).build());
            tripletasGlobales.put("depresion", ResultadosEvaluacionDTO.TripletaGlobalDTO.builder()
                    .T(evaluacion.getDepresionT() != null ? evaluacion.getDepresionT() : BigDecimal.ZERO)
                    .I(evaluacion.getDepresionI() != null ? evaluacion.getDepresionI() : BigDecimal.ZERO)
                    .F(evaluacion.getDepresionF() != null ? evaluacion.getDepresionF() : BigDecimal.ZERO).build());
            tripletasGlobales.put("estres", ResultadosEvaluacionDTO.TripletaGlobalDTO.builder()
                    .T(evaluacion.getEstresT() != null ? evaluacion.getEstresT() : BigDecimal.ZERO)
                    .I(evaluacion.getEstresI() != null ? evaluacion.getEstresI() : BigDecimal.ZERO)
                    .F(evaluacion.getEstresF() != null ? evaluacion.getEstresF() : BigDecimal.ZERO).build());

            ResultadosEvaluacionDTO resultados = ResultadosEvaluacionDTO.builder()
                    .evaluacionId(evaluacionId)
                    .puntajes(puntajes)
                    .alertaCritica(tieneIdeacionSuicida ? "ALERTA CRÍTICA: Ideación suicida detectada" : null)
                    .recomendaciones(recomendacionesDirectas) // Usamos las directas
                    .tripletasGlobales(tripletasGlobales)
                    .build();

            return CompletableFuture.completedFuture(resultados);

        } catch (Exception e) {
            log.error("Error procesando evaluación {}: {}", evaluacionId, e.getMessage(), e);

            Evaluacion evaluacion = evaluacionRepo.findById(evaluacionId).orElse(null);
            if (evaluacion != null) {
                evaluacion.setEstado("error");
                evaluacion.setObservaciones("Error en procesamiento: " + e.getMessage());
                evaluacionRepo.save(evaluacion);
            }

            return CompletableFuture.failedFuture(e);
        }
    }

    private String formatCodigoPrueba(String cuestionario) {
        if ("GAD7".equals(cuestionario))
            return "GAD-7";
        if ("PHQ9".equals(cuestionario))
            return "PHQ-9";
        if ("PSS10".equals(cuestionario))
            return "PSS-10";
        return cuestionario;
    }

    @Override
    @Transactional
    public List<EvaluacionRecomendacion> asignarRecomendacionesPorIds(UUID evaluacionId, List<String> recomendacionIds,
            Map<String, String> criteriosSeleccion) {
        return new ArrayList<>();
    }

    private Map<String, Integer> calcularPuntajes(List<RespuestaCuestionario> respuestas) {
        Map<String, Integer> puntajes = new HashMap<>();

        int gad7 = respuestas.stream()
                .filter(r -> "GAD7".equals(r.getCuestionario()))
                .mapToInt(RespuestaCuestionario::getRespuesta)
                .sum();
        puntajes.put("GAD7", gad7);

        int phq9 = respuestas.stream()
                .filter(r -> "PHQ9".equals(r.getCuestionario()))
                .mapToInt(RespuestaCuestionario::getRespuesta)
                .sum();
        puntajes.put("PHQ9", phq9);

        int pss10 = calcularPss10(respuestas);
        puntajes.put("PSS10", pss10);

        return puntajes;
    }

    private int calcularPss10(List<RespuestaCuestionario> respuestas) {
        int suma = 0;
        for (RespuestaCuestionario r : respuestas) {
            if ("PSS10".equals(r.getCuestionario())) {
                int valor = r.getRespuesta();
                if (PSS10_ITEMS_INVERSOS.contains(r.getNumeroItem())) {
                    valor = 4 - valor;
                }
                suma += valor;
            }
        }
        return suma;
    }

    private String clasificarAnsiedad(int puntaje) {
        if (puntaje <= 4)
            return "minima";
        if (puntaje <= 9)
            return "leve";
        if (puntaje <= 14)
            return "moderada";
        return "severa";
    }

    private String clasificarDepresion(int puntaje) {
        if (puntaje <= 4)
            return "minima";
        if (puntaje <= 9)
            return "leve";
        if (puntaje <= 14)
            return "moderada";
        if (puntaje <= 19)
            return "mod_severa";
        return "severa";
    }

    private String clasificarEstres(int puntaje) {
        if (puntaje <= 13)
            return "bajo";
        if (puntaje <= 26)
            return "moderado";
        return "alto";
    }
}
