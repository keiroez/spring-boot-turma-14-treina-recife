package com.treinarecife.sgp.services;

import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public void inserir(Usuario novoUsuario) {
        usuarioRepository.save(novoUsuario);
    }

    public List<Usuario> buscarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id){
        return usuarioRepository.getReferenceById(id);
    }

    public void atualizar(Usuario novoUsuario){
        usuarioRepository.save(novoUsuario);
    }
}
