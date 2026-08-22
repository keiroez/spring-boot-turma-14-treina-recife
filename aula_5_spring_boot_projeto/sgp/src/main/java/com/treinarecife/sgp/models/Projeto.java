package com.treinarecife.sgp.models;

import com.treinarecife.sgp.models.dto.ProjetoResponse;
import com.treinarecife.sgp.models.enums.StatusProjeto;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Projeto {
    @Id
    @GeneratedValue
    private Long id;
    private String nome, descricao;
    private LocalDate dataInicio, dataConclusao;
    private StatusProjeto status;

    @ManyToOne
    @JoinColumn(name = "id_responsavel")
    private Usuario responsavel;

    public ProjetoResponse toDTO(){
        return new ProjetoResponse(this.id, this.nome, this.descricao);
    }

    public Projeto() {
    }

    public Projeto(String nome, String descricao, LocalDate dataInicio, LocalDate dataConclusao, StatusProjeto status, Usuario responsavel) {
        this.nome = nome;
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataConclusao = dataConclusao;
        this.status = status;
        this.responsavel = responsavel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(LocalDate dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public StatusProjeto getStatus() {
        return status;
    }

    public void setStatus(StatusProjeto status) {
        this.status = status;
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }
}
