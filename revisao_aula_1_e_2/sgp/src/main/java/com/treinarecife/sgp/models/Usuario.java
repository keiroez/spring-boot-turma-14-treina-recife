package com.treinarecife.sgp.models;

import com.treinarecife.sgp.models.dto.UsuarioResponse;
import com.treinarecife.sgp.models.enums.StatusUsuario;

import java.time.LocalDate;

public class Usuario {
    private Long id;
    private String nome, cpf, email, senha;
    private LocalDate dataNascimento;
    private StatusUsuario status;

    public Usuario(Long id, String nome, String cpf, String email, String senha, LocalDate dataNascimento, StatusUsuario status) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
        this.status = status;
    }

    public Usuario(Long responsavel) {
        this.id = responsavel;
    }

    public UsuarioResponse toDTO() {
        return new UsuarioResponse(this);
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public StatusUsuario getStatus() {
        return status;
    }

    public void setStatus(StatusUsuario status) {
        this.status = status;
    }


}
