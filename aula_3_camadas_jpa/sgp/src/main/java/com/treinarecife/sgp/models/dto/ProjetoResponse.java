package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.Projeto;

public record ProjetoResponse(String nome, String descricao) {
    public ProjetoResponse(Projeto projeto) {
        this(projeto.getNome(), projeto.getDescricao());
    }
}
