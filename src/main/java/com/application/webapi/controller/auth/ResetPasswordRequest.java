package com.application.webapi.controller.auth;

public record ResetPasswordRequest(String email, String otp, String newPassword) {
}
