package com.treinarecife.sgp.models.dto;

import com.treinarecife.sgp.models.Usuario;

public record UsuarioResponse(Long id, String nome, String email) {
    public UsuarioResponse(Usuario usuario) {
            this(usuario.getId(), usuario.getNome(), usuario.getEmail()
        );
    }
}
