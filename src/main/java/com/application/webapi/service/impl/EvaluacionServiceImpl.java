package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.*;
import com.application.webapi.repository.*;
import com.application.webapi.service.EvaluacionService;
import com.application.webapi.service.ProcesamientoAsyncService;
import com.application.webapi.service.dto.*;
import com.application.webapi.service.mapper.EvaluacionMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ConsultaMedicaRepository consultaMedicaRepository;
    private final RespuestaCuestionarioRepository respuestaRepository;
    private final EvaluacionRecomendacionRepository evalRecomendacionRepository;
    private final EvaluacionRecomendacionManualRepository evalRecomendacionManualRepository;
    private final EvaluacionMapper evaluacionMapper;
    private final ProcesamientoAsyncService procesamientoAsyncService;

        @Override
        public EvaluacionDTO create(EvaluacionDTO evaluacionDTO) {
                Paciente paciente = pacienteRepository.findById(evaluacionDTO.getPacienteId())
                                .orElseThrow(() -> new EntityNotFoundException("Paciente no encontrado"));
                Usuario usuario = usuarioRepository.findById(evaluacionDTO.getUsuarioId())
                                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));

                Evaluacion entity = evaluacionMapper.toEntity(evaluacionDTO);
                entity.setPaciente(paciente);
                entity.setUsuario(usuario);
                entity.setEstado("en_progreso");

                // Vincular con consulta médica si se proporciona
                if (evaluacionDTO.getConsultaId() != null) {
                        ConsultaMedica consulta = consultaMedicaRepository.findById(evaluacionDTO.getConsultaId())
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                        "Consulta médica no encontrada"));
                        entity.setConsulta(consulta);
                }

                entity = evaluacionRepository.save(entity);
                return evaluacionMapper.toDto(entity);
        }

        @Override
        @Transactional(readOnly = true)
        public Optional<EvaluacionDTO> findById(UUID id) {
                return evaluacionRepository.findById(id)
                                .map(evaluacionMapper::toDto);
        }

        @Override
        @Transactional(readOnly = true)
        public List<EvaluacionDTO> findByPacienteId(UUID pacienteId) {
                return evaluacionRepository.findByPacienteId(pacienteId).stream()
                                .map(evaluacionMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<EvaluacionDTO> findByUsuarioId(UUID usuarioId) {
                return evaluacionRepository.findByUsuarioId(usuarioId).stream()
                                .map(evaluacionMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        public EstadoEvaluacionDTO submitRespuestas(UUID evaluacionId, RespuestasEvaluacionDTO respuestas) {
                Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                                .orElseThrow(() -> new EntityNotFoundException("Evaluación no encontrada"));

                // Validate response counts
                if (respuestas.getGad7() == null || respuestas.getGad7().size() != 7) {
                        throw new IllegalArgumentException("GAD-7 debe tener 7 respuestas");
                }
                if (respuestas.getPhq9() == null || respuestas.getPhq9().size() != 9) {
                        throw new IllegalArgumentException("PHQ-9 debe tener 9 respuestas");
                }
                if (respuestas.getPss10() == null || respuestas.getPss10().size() != 10) {
                        throw new IllegalArgumentException("PSS-10 debe tener 10 respuestas");
                }

                // Save responses
                saveRespuestas(evaluacion, "GAD7", respuestas.getGad7(), 0, 3);
                saveRespuestas(evaluacion, "PHQ9", respuestas.getPhq9(), 0, 3);
                saveRespuestas(evaluacion, "PSS10", respuestas.getPss10(), 0, 4);

                // Update status to processing
                evaluacion.setEstado("procesando");
                evaluacionRepository.save(evaluacion);

                // Trigger async processing after transaction commit to avoid race conditions
                org.springframework.transaction.support.TransactionSynchronizationManager.registerSynchronization(
                                new org.springframework.transaction.support.TransactionSynchronization() {
                                        @Override
                                        public void afterCommit() {
                                                procesamientoAsyncService.procesarEvaluacionAsync(evaluacionId);
                                                log.info("Procesamiento asíncrono iniciado tras commit para evaluación {}",
                                                                evaluacionId);
                                        }
                                });

                log.info("Respuestas recibidas para evaluación {}. Procesamiento programado tras commit.",
                                evaluacionId);

                return EstadoEvaluacionDTO.builder()
                                .evaluacionId(evaluacionId)
                                .estado("procesando")
                                .progreso(0)
                                .completado(false)
                                .mensaje("Respuestas recibidas. Procesamiento en curso...")
                                .resultadosUrl("/api/evaluaciones/" + evaluacionId + "/resultados")
                                .build();
        }

        private void saveRespuestas(Evaluacion evaluacion, String cuestionario,
                        List<RespuestasEvaluacionDTO.RespuestaItemDTO> items,
                        int minValue, int maxValue) {
                for (RespuestasEvaluacionDTO.RespuestaItemDTO item : items) {
                        if (item.getRespuesta() < minValue || item.getRespuesta() > maxValue) {
                                throw new IllegalArgumentException(
                                                String.format("%s ítem %d: respuesta debe estar entre %d y %d",
                                                                cuestionario, item.getNumeroItem(), minValue,
                                                                maxValue));
                        }

                        RespuestaCuestionario respuesta = RespuestaCuestionario.builder()
                                        .evaluacion(evaluacion)
                                        .cuestionario(cuestionario)
                                        .numeroItem(item.getNumeroItem())
                                        .respuesta(item.getRespuesta())
                                        .build();
                        respuestaRepository.save(respuesta);
                }
        }

        @Override
        @Transactional(readOnly = true)
        public EstadoEvaluacionDTO getEstado(UUID evaluacionId) {
                Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                                .orElseThrow(() -> new EntityNotFoundException("Evaluación no encontrada"));

                String estado = evaluacion.getEstado();
                boolean completado = "completada".equals(estado);
                boolean error = "error".equals(estado);

                int progreso;
                String mensaje;
                if (completado) {
                        progreso = 100;
                        mensaje = "Evaluación completada";
                } else if (error) {
                        progreso = 0;
                        mensaje = evaluacion.getObservaciones();
                } else if ("procesando".equals(estado)) {
                        progreso = 50;
                        mensaje = "Procesamiento en curso...";
                } else {
                        progreso = 10;
                        mensaje = "Esperando respuestas...";
                }

                return EstadoEvaluacionDTO.builder()
                                .evaluacionId(evaluacionId)
                                .estado(estado)
                                .progreso(progreso)
                                .completado(completado)
                                .mensaje(mensaje)
                                .resultadosUrl(completado ? "/api/evaluaciones/" + evaluacionId + "/resultados" : null)
                                .error(error ? evaluacion.getObservaciones() : null)
                                .build();
        }

        @Override
        @Transactional(readOnly = true)
        public ResultadosEvaluacionDTO getResultados(UUID evaluacionId) {
                Evaluacion evaluacion = evaluacionRepository.findById(evaluacionId)
                                .orElseThrow(() -> new EntityNotFoundException("Evaluación no encontrada"));

                if (!"completada".equals(evaluacion.getEstado())) {
                        throw new IllegalStateException("La evaluación aún no está completada");
                }

                Paciente paciente = evaluacion.getPaciente();

                // Build puntajes map
                Map<String, Integer> puntajes = new HashMap<>();
                puntajes.put("gad7", evaluacion.getGad7Puntaje());
                puntajes.put("phq9", evaluacion.getPhq9Puntaje());
                puntajes.put("pss10", evaluacion.getPss10Puntaje());

                // Build niveles map
                Map<String, String> niveles = new HashMap<>();
                niveles.put("ansiedad", evaluacion.getNivelAnsiedad());
                niveles.put("depresion", evaluacion.getNivelDepresion());
                niveles.put("estres", evaluacion.getNivelEstres());

                // Build tripletas globales
                Map<String, ResultadosEvaluacionDTO.TripletaGlobalDTO> tripletasGlobales = new HashMap<>();
                tripletasGlobales.put("ansiedad", ResultadosEvaluacionDTO.TripletaGlobalDTO.builder()
                                .T(evaluacion.getAnsiedadT() != null ? evaluacion.getAnsiedadT() : BigDecimal.ZERO)
                                .I(evaluacion.getAnsiedadI() != null ? evaluacion.getAnsiedadI() : BigDecimal.ZERO)
                                .F(evaluacion.getAnsiedadF() != null ? evaluacion.getAnsiedadF() : BigDecimal.ZERO)
                                .build());
                tripletasGlobales.put("depresion", ResultadosEvaluacionDTO.TripletaGlobalDTO.builder()
                                .T(evaluacion.getDepresionT() != null ? evaluacion.getDepresionT() : BigDecimal.ZERO)
                                .I(evaluacion.getDepresionI() != null ? evaluacion.getDepresionI() : BigDecimal.ZERO)
                                .F(evaluacion.getDepresionF() != null ? evaluacion.getDepresionF() : BigDecimal.ZERO)
                                .build());
                tripletasGlobales.put("estres", ResultadosEvaluacionDTO.TripletaGlobalDTO.builder()
                                .T(evaluacion.getEstresT() != null ? evaluacion.getEstresT() : BigDecimal.ZERO)
                                .I(evaluacion.getEstresI() != null ? evaluacion.getEstresI() : BigDecimal.ZERO)
                                .F(evaluacion.getEstresF() != null ? evaluacion.getEstresF() : BigDecimal.ZERO)
                                .build());

                // Build probabilidades (mapped to test percentages)
                ResultadosEvaluacionDTO.ProbabilidadesDTO probabilidades = ResultadosEvaluacionDTO.ProbabilidadesDTO
                                .builder()
                                .ansiedad(evaluacion.getPorcentajeAnsiedad())
                                .depresion(evaluacion.getPorcentajeDepresion())
                                .estres(evaluacion.getPorcentajeEstres())
                                .build();

                // Get recommendations - NEW LOGIC: Try manual first, then fallback to linked? 
                // Currently user wants the manual table ones which are direct strings.
                List<EvaluacionRecomendacionManual> manualRecs = evalRecomendacionManualRepository
                                .findByEvaluacionIdOrderByFechaAsignacionAsc(evaluacionId);
                                
                List<String> recomendaciones;
                if (!manualRecs.isEmpty()) {
                     recomendaciones = manualRecs.stream()
                        .map(EvaluacionRecomendacionManual::getRecomendacionTexto)
                        .collect(Collectors.toList());
                } else {
                    // Fallback to old logic just in case? Or pure separation?
                    // Let's keep fallback for old evaluations if any
                    List<EvaluacionRecomendacion> evalRecomendaciones = evalRecomendacionRepository
                                    .findByEvaluacionIdOrderByOrdenPresentacionAsc(evaluacionId);
                    recomendaciones = evalRecomendaciones.stream()
                                    .map(er -> er.getRecomendacion().getDescripcion())
                                    .collect(Collectors.toList());
                }

                return ResultadosEvaluacionDTO.builder()
                                .evaluacionId(evaluacionId)
                                .fechaEvaluacion(evaluacion.getFechaHora())
                                .paciente(ResultadosEvaluacionDTO.PacienteInfo.builder()
                                                .id(paciente.getId())
                                                .edad(paciente.getEdad())
                                                .genero(paciente.getGenero())
                                                .enfermedadCronica(paciente.getEnfermedadCronica())
                                                .build())
                                .puntajes(puntajes)
                                .niveles(niveles)
                                .tripletasGlobales(tripletasGlobales)
                                .probabilidadesAdherencia(probabilidades)
                                .recomendaciones(recomendaciones)
                                .alertaCritica(evaluacion.getObservaciones() != null
                                                && evaluacion.getObservaciones().contains("ALERTA")
                                                                ? evaluacion.getObservaciones()
                                                                : null)
                                .build();
        }

        @Override
        @Transactional(readOnly = true)
        public RespuestasEvaluacionDTO getRespuestas(UUID evaluacionId) {
                if (!evaluacionRepository.existsById(evaluacionId)) {
                        throw new EntityNotFoundException("Evaluación no encontrada");
                }

                List<RespuestaCuestionario> respuestasEntity = respuestaRepository.findByEvaluacionId(evaluacionId);

                List<RespuestasEvaluacionDTO.RespuestaItemDTO> gad7 = new ArrayList<>();
                List<RespuestasEvaluacionDTO.RespuestaItemDTO> phq9 = new ArrayList<>();
                List<RespuestasEvaluacionDTO.RespuestaItemDTO> pss10 = new ArrayList<>();

                for (RespuestaCuestionario r : respuestasEntity) {
                        RespuestasEvaluacionDTO.RespuestaItemDTO item = RespuestasEvaluacionDTO.RespuestaItemDTO
                                        .builder()
                                        .numeroItem(r.getNumeroItem())
                                        .respuesta(r.getRespuesta())
                                        .build();

                        switch (r.getCuestionario()) {
                                case "GAD7":
                                        gad7.add(item);
                                        break;
                                case "PHQ9":
                                        phq9.add(item);
                                        break;
                                case "PSS10":
                                        pss10.add(item);
                                        break;
                        }
                }

                // Sort by item number
                gad7.sort(Comparator.comparingInt(RespuestasEvaluacionDTO.RespuestaItemDTO::getNumeroItem));
                phq9.sort(Comparator.comparingInt(RespuestasEvaluacionDTO.RespuestaItemDTO::getNumeroItem));
                pss10.sort(Comparator.comparingInt(RespuestasEvaluacionDTO.RespuestaItemDTO::getNumeroItem));

                return RespuestasEvaluacionDTO.builder()
                                .gad7(gad7)
                                .phq9(phq9)
                                .pss10(pss10)
                                .build();
        }
}
