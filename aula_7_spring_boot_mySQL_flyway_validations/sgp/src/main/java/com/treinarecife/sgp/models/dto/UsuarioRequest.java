package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.enums.StatusUsuario;

import java.time.LocalDate;

public record UsuarioRequest(
        String nome,
        String cpf,
        String email,
        String senha,
        LocalDate dataNascimento,
        StatusUsuario status
) {
}
