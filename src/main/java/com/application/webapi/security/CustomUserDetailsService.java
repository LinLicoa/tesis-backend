package com.application.webapi.security;

import com.application.webapi.domain.entity.Usuario;
import com.application.webapi.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

        private final UsuarioRepository usuarioRepository;

        @Override
        public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
                Usuario usuario = usuarioRepository.findByEmail(email)
                                .orElseThrow(() -> new UsernameNotFoundException(
                                                "Usuario no encontrado con email: " + email));

                boolean accountNonLocked = usuario.getBloqueadoHasta() == null
                                || usuario.getBloqueadoHasta().isBefore(java.time.LocalDateTime.now());

                return new org.springframework.security.core.userdetails.User(
                                usuario.getEmail(),
                                usuario.getPassword(),
                                usuario.isActivo(),
                                true,
                                true,
                                accountNonLocked,
                                java.util.Collections.singletonList(
                                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                                                "ROLE_" + usuario.getRol().toUpperCase())));
        }
}
