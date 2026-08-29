package com.exemplo.gestao.dto;

import com.exemplo.gestao.model.enums.StatusProjeto;

import java.time.LocalDate;

public record ProjetoResponse(
        Long id,
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataConclusao,
        StatusProjeto status,
        Long responsavelId,
        String responsavelNome
) {
}
