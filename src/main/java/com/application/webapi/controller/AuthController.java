package com.application.webapi.controller;

import com.application.webapi.controller.auth.AuthResponse;
import com.application.webapi.controller.auth.ChangePasswordRequest;
import com.application.webapi.controller.auth.ForgotPasswordRequest;
import com.application.webapi.controller.auth.LoginRequest;
import com.application.webapi.controller.auth.RegisterRequest;
import com.application.webapi.controller.auth.ResetPasswordRequest;
import com.application.webapi.controller.auth.ValidateOtpRequest;
import com.application.webapi.service.dto.UsuarioDTO;
import com.application.webapi.domain.entity.Usuario;
import com.application.webapi.security.JwtService;
import com.application.webapi.service.UsuarioService;
import com.application.webapi.service.mapper.UsuarioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UsuarioService usuarioService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UsuarioMapper usuarioMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombreCompleto(request.nombreCompleto());
        dto.setCedulaProfesional(request.cedulaProfesional());
        dto.setEspecialidad(request.especialidad());
        dto.setEmail(request.email());
        dto.setPassword(request.password());
        dto.setActivo(true);
        // Default role or determined by internal logic
        dto.setRol("usuario"); // Default role matching DB constraint 'usuario'

        Usuario usuario = usuarioMapper.toEntity(dto);
        Usuario savedUser = usuarioService.save(usuario);

        // After registration, generate token with extra claims
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("id", savedUser.getId());
        extraClaims.put("rol", savedUser.getRol());
        extraClaims.put("cedula_profesional", savedUser.getCedulaProfesional());

        String jwtToken = jwtService.generateToken(extraClaims, userDetails);

        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()));
            usuarioService.loginSucceeded(request.email());
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            usuarioService.loginFailed(request.email());
            throw e;
        }

        Usuario usuario = usuarioService.findByEmail(request.email()).orElseThrow();
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());

        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("id", usuario.getId());
        extraClaims.put("rol", usuario.getRol());
        extraClaims.put("cedula_profesional", usuario.getCedulaProfesional());

        String jwtToken = jwtService.generateToken(extraClaims, userDetails);

        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<java.util.Map<String, String>> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        usuarioService.initiatePasswordReset(request.email());
        return ResponseEntity
                .ok(java.util.Map.of("message", "Si el correo existe, se ha enviado un código de verificación."));
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<java.util.Map<String, Object>> validateOtp(@RequestBody ValidateOtpRequest request) {
        boolean isValid = usuarioService.validateOtp(request.email(), request.otp());
        if (isValid) {
            return ResponseEntity.ok(java.util.Map.of("valid", true, "message", "Código válido."));
        } else {
            return ResponseEntity.status(400)
                    .body(java.util.Map.of("valid", false, "message", "Código inválido o expirado."));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<java.util.Map<String, String>> resetPassword(@RequestBody ResetPasswordRequest request) {
        usuarioService.completePasswordReset(request.email(), request.otp(), request.newPassword());
        return ResponseEntity.ok(java.util.Map.of("message", "Contraseña restablecida correctamente."));
    }

    @PostMapping("/change-password")
    public ResponseEntity<java.util.Map<String, String>> changePassword(
            @RequestBody ChangePasswordRequest request,
            java.security.Principal principal) {
        String email = principal.getName();
        usuarioService.changePassword(email, request.currentPassword(), request.newPassword());
        return ResponseEntity.ok(java.util.Map.of("message", "Contraseña actualizada correctamente."));
    }
}
