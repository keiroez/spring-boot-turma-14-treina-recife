package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.Usuario;

public record UsuarioResponse(String nome, String email) {
    public UsuarioResponse(Usuario usuario) {
        this(usuario.getNome(), usuario.getEmail());
    }
}
