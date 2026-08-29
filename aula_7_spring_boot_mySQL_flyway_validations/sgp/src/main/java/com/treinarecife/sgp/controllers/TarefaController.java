package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Tarefa;
import com.treinarecife.sgp.models.dto.TarefaRequest;
import com.treinarecife.sgp.models.dto.TarefaResponse;
import com.treinarecife.sgp.services.TarefaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {
    
    @Autowired
    private TarefaService tarefaService;

    @GetMapping
    public List<TarefaResponse> buscarTodos(){
        List<TarefaResponse> listaRetorno = new ArrayList<>();
        //Fui buscar todos no banco de dados
        List<Tarefa> tarefas = tarefaService.buscarTodos();
        for(Tarefa tarefa : tarefas){
            TarefaResponse TarefaResponse = tarefa.toDTO();
            listaRetorno.add(TarefaResponse);
        }
        return listaRetorno;
    }

    @GetMapping("/{id}")
    public TarefaResponse buscarPorId(@PathVariable Long id){
        Tarefa Tarefa = tarefaService.buscarPorId(id);
        return Tarefa.toDTO();
    }

    @PostMapping
    public TarefaResponse inserir(@RequestBody TarefaRequest TarefaRequest){
        Tarefa Tarefa = tarefaService.inserir(TarefaRequest);
        return Tarefa.toDTO();
    }

    @PutMapping("/{id}")
    public TarefaResponse atualizar(@PathVariable Long id, @RequestBody TarefaRequest TarefaRequest){
        var Tarefa = tarefaService.atualizar(id, TarefaRequest);
        return Tarefa.toDTO();
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        tarefaService.deletar(id);
        return "Tarefa deletado com sucesso!";
    }
}
