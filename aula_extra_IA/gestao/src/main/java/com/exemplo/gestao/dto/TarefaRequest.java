package com.exemplo.gestao.dto;

import com.exemplo.gestao.model.enums.Prioridade;
import com.exemplo.gestao.model.enums.StatusTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TarefaRequest(

        @NotBlank(message = "Titulo e obrigatorio")
        String titulo,

        String descricao,

        LocalDate dataConclusao,

        @NotNull(message = "Prioridade e obrigatoria")
        Prioridade prioridade,

        @NotNull(message = "Status e obrigatorio")
        StatusTarefa status,

        Long projetoId,

        Long usuarioId
) {
}
