package com.treinarecife.sgp.models;

import com.treinarecife.sgp.models.dto.TarefaResponse;
import com.treinarecife.sgp.models.enums.Prioridade;
import com.treinarecife.sgp.models.enums.StatusTarefa;
import com.treinarecife.sgp.services.TarefaService;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Tarefa {
    @Id
    @GeneratedValue
    private Long id;
    private String titulo, descricao;
    private LocalDate dataCriacao, dataConclusao;
    private Prioridade prioridade;
    private StatusTarefa status;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public TarefaResponse toDTO(){
        return new TarefaResponse(this.id, this.titulo, this.descricao);
    }

    public Tarefa(String titulo, String descricao, LocalDate dataCriacao, LocalDate dataConclusao, Prioridade prioridade, StatusTarefa status, Usuario usuario) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataCriacao = dataCriacao;
        this.dataConclusao = dataConclusao;
        this.prioridade = prioridade;
        this.status = status;
        this.usuario = usuario;
    }

    public Tarefa() {
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

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

