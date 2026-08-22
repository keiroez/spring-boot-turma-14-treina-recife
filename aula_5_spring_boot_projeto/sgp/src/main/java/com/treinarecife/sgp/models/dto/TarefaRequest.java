package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.enums.Prioridade;
import com.treinarecife.sgp.models.enums.StatusTarefa;


import java.time.LocalDate;

public record TarefaRequest(
        String titulo,
        String descricao,
        LocalDate dataCriacao,
        LocalDate dataConclusao,
        Prioridade prioridade,
        StatusTarefa status,
        Long usuario
) {
}
