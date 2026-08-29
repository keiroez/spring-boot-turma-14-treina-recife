package com.exemplo.gestao.dto;

import com.exemplo.gestao.model.enums.StatusProjeto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ProjetoRequest(

        @NotBlank(message = "Nome do projeto e obrigatorio")
        String nome,

        String descricao,

        @NotNull(message = "Data de inicio e obrigatoria")
        LocalDate dataInicio,

        LocalDate dataConclusao,

        @NotNull(message = "Status e obrigatorio")
        StatusProjeto status,

        @NotNull(message = "Id do responsavel e obrigatorio")
        Long responsavelId
) {
}
