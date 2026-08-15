package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.Tarefa;

public record TarefaResponse(String titulo, String descricao) {
    public TarefaResponse(Tarefa tarefa) {
        this(tarefa.getTitulo(), tarefa.getDescricao());
    }
}
