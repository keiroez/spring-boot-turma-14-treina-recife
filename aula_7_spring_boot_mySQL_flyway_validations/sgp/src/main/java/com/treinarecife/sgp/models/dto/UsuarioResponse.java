package com.treinarecife.sgp.models.dto;

public record UsuarioResponse(
        Long id,
        String nome,
        String cpf,
        String email
) {
}
