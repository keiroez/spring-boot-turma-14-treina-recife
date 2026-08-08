package com.gestaoTarefas.app.controllers.requests;

import com.gestaoTarefas.app.models.enums.Prioridade;

public record TarefaRequest(String titulo, String descricao, Prioridade prioridade) {
}
