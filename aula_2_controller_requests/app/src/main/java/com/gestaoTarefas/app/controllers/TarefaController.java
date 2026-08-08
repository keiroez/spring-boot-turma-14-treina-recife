package com.gestaoTarefas.app.controllers;

import com.gestaoTarefas.app.controllers.requests.TarefaResponse;
import com.gestaoTarefas.app.models.Tarefa;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private List<Tarefa> tarefas = new ArrayList<>();


    @GetMapping
    public List<TarefaResponse> listar() {
        List<TarefaResponse> resultado = new ArrayList<>();
        for (Tarefa t : tarefas) {
            resultado.add(toResponse(t));
        }
        return resultado;
    }
    @GetMapping("/{id}")
    public TarefaResponse buscarPorId(@PathVariable Long id) {
        for (Tarefa t : tarefas) {
            if (t.getId().equals(id)) return toResponse(t);
        }
        throw new RuntimeException("Tarefa não encontrada");
    }

    public TarefaResponse toResponse(Tarefa t) {
        return new TarefaResponse(t.getId(), t.getTitulo(), t.getDescricao());
    }




}
