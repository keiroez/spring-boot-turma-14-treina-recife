package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.enums.StatusUsuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record UsuarioRequest(
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        String cpf,
        @Email(message = "E-mail inválido")
        String email,
        String senha,
        LocalDate dataNascimento,
        StatusUsuario status
) {
}
