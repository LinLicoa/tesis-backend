package com.application.webapi.service.impl;

import com.application.webapi.domain.entity.Usuario;
import com.application.webapi.repository.UsuarioRepository;
import com.application.webapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Usuario save(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario createAdmin(Usuario usuario) {
        usuario.setRol("ADMIN");
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> findById(UUID id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    private final com.application.webapi.repository.ParametroSistemaRepository parametroSistemaRepository;

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    @Override
    public void loginFailed(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElse(null);
        if (usuario == null)
            return;

        int maxIntentos = Integer.parseInt(parametroSistemaRepository.findByClave("MAX_INTENTOS_LOGIN")
                .map(p -> p.getValor())
                .orElse("3"));

        usuario.setIntentosFallidos(usuario.getIntentosFallidos() + 1);

        if (usuario.getIntentosFallidos() >= maxIntentos) {
            int minutosBloqueo = Integer.parseInt(parametroSistemaRepository.findByClave("TIEMPO_BLOQUEO_MINUTOS")
                    .map(p -> p.getValor())
                    .orElse("15"));
            usuario.setBloqueadoHasta(java.time.LocalDateTime.now().plusMinutes(minutosBloqueo));
        }
        usuarioRepository.save(usuario);
    }

    @Override
    public void loginSucceeded(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (usuario == null)
            return;

        usuario.setIntentosFallidos(0);
        usuario.setBloqueadoHasta(null);
        usuario.setUltimoAcceso(java.time.LocalDateTime.now());
        usuarioRepository.save(usuario);
    }

    private final com.application.webapi.repository.TokenRecuperacionRepository tokenRecuperacionRepository;
    private final com.application.webapi.service.EmailService emailService;

    private final org.thymeleaf.TemplateEngine templateEngine;

    @Override
    public void initiatePasswordReset(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Generate 6 digit OTP
        String otp = String.format("%06d", new java.util.Random().nextInt(999999));

        com.application.webapi.domain.entity.TokenRecuperacion tokenEntity = com.application.webapi.domain.entity.TokenRecuperacion
                .builder()
                .usuario(usuario)
                .token(otp)
                .fechaExpiracion(java.time.LocalDateTime.now().plusMinutes(15))
                .build();

        tokenRecuperacionRepository.save(tokenEntity);

        org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
        context.setVariable("otp", otp);

        String htmlContent = templateEngine.process("email-recovery", context);

        emailService.sendEmail(email, "Recuperación de Contraseña - NeuroSalud", htmlContent, true);
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return tokenRecuperacionRepository.findByToken(otp)
                .map(token -> !token.isUsado()
                        && token.getFechaExpiracion().isAfter(java.time.LocalDateTime.now())
                        && token.getUsuario().getId().equals(usuario.getId()))
                .orElse(false);
    }

    @Override
    public void completePasswordReset(String email, String otp, String newPassword) {
        if (!validateOtp(email, otp)) {
            throw new RuntimeException("Código OTP inválido o expirado");
        }

        com.application.webapi.domain.entity.TokenRecuperacion tokenEntity = tokenRecuperacionRepository
                .findByToken(otp)
                .orElseThrow(() -> new RuntimeException("Error interno: token no encontrado"));

        Usuario usuario = tokenEntity.getUsuario();
        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);

        tokenEntity.setUsado(true);
        tokenEntity.setFechaUso(java.time.LocalDateTime.now());
        tokenRecuperacionRepository.save(tokenEntity);
    }

    @Override
    public void changePassword(String email, String currentPassword, String newPassword) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(currentPassword, usuario.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuarioRepository.save(usuario);
    }
}
