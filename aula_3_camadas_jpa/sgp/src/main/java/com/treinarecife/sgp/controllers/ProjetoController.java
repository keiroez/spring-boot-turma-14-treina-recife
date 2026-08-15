package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Projeto;
import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.dto.ProjetoRequest;
import com.treinarecife.sgp.models.dto.ProjetoResponse;
import com.treinarecife.sgp.services.ProjetoService;
import com.treinarecife.sgp.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {
    @Autowired
    private ProjetoService projetoService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<ProjetoResponse> listar() {
        List<ProjetoResponse> resultado = new ArrayList<>();
        List<Projeto> projetos = projetoService.buscarTodos();
        for (Projeto t : projetos) {
            resultado.add(t.toDTO());
        }
        return resultado;
    }
    @GetMapping("/{id}")
    public ProjetoResponse buscarPorId(@PathVariable Long id) {
        Projeto projeto = projetoService.buscarPorId(id);
        return projeto.toDTO();
    }

    @PostMapping
    public ProjetoResponse criar(@RequestBody ProjetoRequest req) {
        Usuario usuario = usuarioService.buscarPorId(req.responsavel());
        Projeto nova = new Projeto(
                req.nome(),
                req.descricao(),
                req.dataInicio(),
                req.dataConclusao(),
                req.status(),
                usuario
        );
        projetoService.inserir(nova);
        return nova.toDTO();
    }

    @PutMapping("/{id}")
    public ProjetoResponse atualizar(@PathVariable Long id, @RequestBody ProjetoRequest req) {
        Projeto projeto = projetoService.buscarPorId(id);
        projeto.setNome(req.nome());
        projeto.setDescricao(req.descricao());

        projetoService.atualizar(projeto);

        return projeto.toDTO();
    }
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        projetoService.deletar(id);
    }
}
