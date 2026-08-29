package com.exemplo.gestao.dto;

import com.exemplo.gestao.model.enums.Prioridade;
import com.exemplo.gestao.model.enums.StatusTarefa;

import java.time.LocalDate;

public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        LocalDate dataCriacao,
        LocalDate dataConclusao,
        Prioridade prioridade,
        StatusTarefa status,
        Long projetoId,
        Long usuarioId
) {
}
