package com.application.webapi.service;

import com.application.webapi.domain.entity.Usuario;
import java.util.Optional;
import java.util.UUID;

public interface UsuarioService {
    Usuario save(Usuario usuario);

    Optional<Usuario> findById(UUID id);

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    void loginFailed(String email);

    void loginSucceeded(String email);

    Usuario createAdmin(Usuario usuario);

    void initiatePasswordReset(String email);

    boolean validateOtp(String email, String otp);

    void completePasswordReset(String email, String otp, String newPassword);

    void changePassword(String email, String currentPassword, String newPassword);
}
