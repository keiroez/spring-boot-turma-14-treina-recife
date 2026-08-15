package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.enums.StatusProjeto;

import java.time.LocalDate;

public record ProjetoRequest(
        String nome,
        String descricao,
        LocalDate dataInicio,
        LocalDate dataConclusao,
        StatusProjeto status,
        Long responsavel
) {
}
