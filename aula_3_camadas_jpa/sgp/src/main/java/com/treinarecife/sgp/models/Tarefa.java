package com.treinarecife.sgp.models;

import com.treinarecife.sgp.models.dto.TarefaResponse;
import com.treinarecife.sgp.models.enums.Prioridade;
import com.treinarecife.sgp.models.enums.StatusTarefa;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Tarefa {

    @Id
    @GeneratedValue
    private Long id;
    private String titulo, descricao;
    private Prioridade prioridade;
    private StatusTarefa status;

    public Tarefa() {
    }

    public Tarefa(Long id, String titulo, String descricao, Prioridade prioridade,
                  StatusTarefa status) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.status = status;
    }

    public TarefaResponse toDTO() {
        return new TarefaResponse(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }
}
