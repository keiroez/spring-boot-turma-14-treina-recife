package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.dto.UsuarioRequest;
import com.treinarecife.sgp.models.dto.UsuarioResponse;
import com.treinarecife.sgp.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> listar() {
        List<UsuarioResponse> resultado = new ArrayList<>();
        List<Usuario> usuarios = usuarioService.buscarTodos();
        for (Usuario t : usuarios) {
            resultado.add(t.toDTO());
        }
        return resultado;
    }
    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        return usuario.toDTO();
    }

    @PostMapping
    public UsuarioResponse criar(@RequestBody UsuarioRequest req) {
        Usuario nova = new Usuario(req.nome(), req.cpf(),req.email(), req.senha(), req.dataNascimento(), req.status());
        usuarioService.inserir(nova);
        return nova.toDTO();
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody UsuarioRequest req) {
        Usuario usuario = usuarioService.buscarPorId(id);
        usuario.setNome(req.nome());
        usuario.setCpf(req.cpf());
        usuario.setEmail(req.email());
        usuario.setSenha(req.senha());
        usuario.setDataNascimento(req.dataNascimento());

        usuarioService.atualizar(usuario);

        return usuario.toDTO();
    }
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        usuarioService.deletar(id);
    }
}
