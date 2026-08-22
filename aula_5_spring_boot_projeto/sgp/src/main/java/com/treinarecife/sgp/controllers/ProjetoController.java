package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Projeto;
import com.treinarecife.sgp.models.dto.ProjetoRequest;
import com.treinarecife.sgp.models.dto.ProjetoResponse;
import com.treinarecife.sgp.services.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @GetMapping
    public List<ProjetoResponse> buscarTodos(){
        List<ProjetoResponse> listaRetorno = new ArrayList<>();
        //Fui buscar todos no banco de dados
        List<Projeto> projetos = projetoService.buscarTodos();
        for(Projeto projeto : projetos){
            ProjetoResponse projetoResponse = projeto.toDTO();
            listaRetorno.add(projetoResponse);
        }
        return listaRetorno;
    }

    @GetMapping("/{id}")
    public ProjetoResponse buscarPorId(@PathVariable Long id){
        Projeto projeto = projetoService.buscarPorId(id);
        return projeto.toDTO();
    }

    @PostMapping
    public ProjetoResponse inserir(@RequestBody ProjetoRequest projetoRequest){
        Projeto projeto = projetoService.inserir(projetoRequest);
        return projeto.toDTO();
    }

    @PutMapping("/{id}")
    public ProjetoResponse atualizar(@PathVariable Long id, @RequestBody ProjetoRequest projetoRequest){
        var projeto = projetoService.atualizar(id, projetoRequest);
        return projeto.toDTO();
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        projetoService.deletar(id);
        return "Projeto deletado com sucesso!";
    }
}
