package com.exemplo.gestao.dto;

import com.exemplo.gestao.model.enums.StatusUsuario;

import java.time.LocalDate;

public record UsuarioResponse(
        Long id,
        String nome,
        String cpf,
        String email,
        LocalDate dataNascimento,
        StatusUsuario status
) {
}
