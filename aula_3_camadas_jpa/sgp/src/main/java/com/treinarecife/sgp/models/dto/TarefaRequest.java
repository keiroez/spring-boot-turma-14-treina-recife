package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.enums.Prioridade;

public record TarefaRequest(String titulo, String descricao, Prioridade prioridade) {
}
