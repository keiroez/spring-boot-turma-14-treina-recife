package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Projeto;
import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.dto.ProjetoRequest;
import com.treinarecife.sgp.models.dto.ProjetoResponse;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {
    private List<Projeto> projetos = new ArrayList<>();

    @GetMapping
    public List<ProjetoResponse> listar() {
        List<ProjetoResponse> resultado = new ArrayList<>();
        for (Projeto t : projetos) {
            resultado.add(t.toDTO());
        }
        return resultado;
    }
    @GetMapping("/{id}")
    public ProjetoResponse buscarPorId(@PathVariable Long id) {
        for (Projeto t : projetos) {
            if (t.getId().equals(id)) return t.toDTO();
        }
        throw new RuntimeException("Tarefa não encontrada");
    }

    @PostMapping
    public ProjetoResponse criar(@RequestBody ProjetoRequest req) {
        Long proximoId = projetos.size()+1L;
        Usuario usuario = new Usuario(req.responsavel());
        Projeto nova = new Projeto(
                proximoId,
                req.nome(),
                req.descricao(),
                req.dataInicio(),
                req.dataConclusao(),
                req.status(),
                usuario
        );
        projetos.add(nova);
        return nova.toDTO();
    }

    @PutMapping("/{id}")
    public ProjetoResponse atualizar(@PathVariable Long id, @RequestBody ProjetoRequest req) {
        for (Projeto u : projetos) {
            if (u.getId().equals(id)) {
                u.setDescricao(req.descricao());
                return u.toDTO();
            }
        }
        throw new RuntimeException("Tarefa não encontrada");
    }
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        for (int i = 0; i < projetos.size(); i++) {
            if (projetos.get(i).getId().equals(id)) projetos.remove(i);
        }
    }
}
