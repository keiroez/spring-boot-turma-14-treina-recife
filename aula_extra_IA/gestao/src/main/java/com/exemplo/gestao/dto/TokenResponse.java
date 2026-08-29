package com.exemplo.gestao.dto;

public record TokenResponse(
        String token,
        String tipo,
        String email
) {
    public TokenResponse(String token, String email) {
        this(token, "Bearer", email);
    }
}
