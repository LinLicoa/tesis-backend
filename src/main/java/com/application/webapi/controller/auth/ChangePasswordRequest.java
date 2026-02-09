package com.application.webapi.controller.auth;

public record ChangePasswordRequest(String currentPassword, String newPassword) {
}
