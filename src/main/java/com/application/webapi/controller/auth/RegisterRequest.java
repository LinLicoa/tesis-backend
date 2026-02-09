package com.application.webapi.controller.auth;

public record RegisterRequest(
        String nombreCompleto,
        String cedulaProfesional,
        String especialidad,
        String email,
        String password) {
}
