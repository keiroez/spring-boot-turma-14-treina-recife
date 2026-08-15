package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.dto.UsuarioRequest;
import com.treinarecife.sgp.models.dto.UsuarioResponse;
import com.treinarecife.sgp.services.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    private List<Usuario> usuarios = new ArrayList<>();

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        List<UsuarioResponse> resultado = new ArrayList<>();
        for (Usuario t : usuarios) {
            resultado.add(t.toDTO());
        }
        return resultado;
    }
    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        for (Usuario t : usuarios) {
            if (t.getId().equals(id)) return t.toDTO();
        }
        throw new RuntimeException("Tarefa não encontrada");
    }

    @PostMapping
    public UsuarioResponse criar(@RequestBody UsuarioRequest req) {
        Usuario nova = new Usuario(req.nome(), req.cpf(),req.email(), req.senha(), req.dataNascimento(), req.status());
        usuarioService.inserir(nova);
        return nova.toDTO();
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody UsuarioRequest req) {
        for (Usuario u : usuarios) {
            if (u.getId().equals(id)) {
                u.setEmail(req.email());
                return u.toDTO();
            }
        }
        throw new RuntimeException("Tarefa não encontrada");
    }
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getId().equals(id)) usuarios.remove(i);
        }
    }
}
