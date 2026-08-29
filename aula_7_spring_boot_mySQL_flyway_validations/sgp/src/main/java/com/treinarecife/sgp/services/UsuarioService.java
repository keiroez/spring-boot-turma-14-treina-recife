package com.treinarecife.sgp.services;

import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.dto.UsuarioRequest;
import com.treinarecife.sgp.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> buscarTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(long id){
        return usuarioRepository.getReferenceById(id);
    }

    public Usuario inserir(UsuarioRequest usuarioRequest){
        Usuario usuario = new Usuario(
                usuarioRequest.nome(),
                usuarioRequest.cpf(),
                usuarioRequest.email(),
                usuarioRequest.senha(),
                usuarioRequest.dataNascimento(),
                usuarioRequest.status()
        );

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(Long id, UsuarioRequest usuarioRequest){
        Usuario usuario = usuarioRepository.getReferenceById(id);
        usuario.setNome(usuarioRequest.nome());
        usuario.setCpf(usuarioRequest.cpf());
        usuario.setEmail(usuarioRequest.email());
        usuario.setSenha(usuarioRequest.senha());
        usuario.setDataNascimento(usuarioRequest.dataNascimento());
        usuario.setStatus(usuarioRequest.status());

        return usuarioRepository.save(usuario);
    }

    public void deletar(Long id){
        usuarioRepository.deleteById(id);
    }
}
