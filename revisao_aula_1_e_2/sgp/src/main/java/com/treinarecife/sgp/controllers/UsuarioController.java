package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.dto.UsuarioRequest;
import com.treinarecife.sgp.models.dto.UsuarioResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("usuarios")
public class UsuarioController {


    private List<Usuario> usuarios = new ArrayList<>();

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
        Long proximoId = usuarios.size()+1L;
        Usuario nova = new Usuario(proximoId, req.nome(), req.cpf(),req.email(), req.senha(), req.dataNascimento(), req.status());
        usuarios.add(nova);
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
