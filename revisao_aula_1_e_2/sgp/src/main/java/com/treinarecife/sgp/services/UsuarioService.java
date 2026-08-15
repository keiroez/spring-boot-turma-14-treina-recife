package com.treinarecife.sgp.services;

import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public void inserir(Usuario novoUsuario) {
        usuarioRepository.save(novoUsuario);
    }
}
