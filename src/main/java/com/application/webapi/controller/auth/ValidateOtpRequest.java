package com.application.webapi.controller.auth;

public record ValidateOtpRequest(String email, String otp) {
}
