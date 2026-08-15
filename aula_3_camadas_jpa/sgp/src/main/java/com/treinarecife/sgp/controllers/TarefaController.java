package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Tarefa;
import com.treinarecife.sgp.models.dto.TarefaRequest;
import com.treinarecife.sgp.models.dto.TarefaResponse;
import com.treinarecife.sgp.models.enums.StatusTarefa;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("tarefas")
public class TarefaController {

    private List<Tarefa> tarefas = new ArrayList<>();

    @GetMapping
    public List<TarefaResponse> listar() {
        List<TarefaResponse> resultado = new ArrayList<>();
        for (Tarefa t : tarefas) {
            resultado.add(t.toDTO());
        }
        return resultado;
    }
    @GetMapping("/{id}")
    public TarefaResponse buscarPorId(@PathVariable Long id) {
        for (Tarefa t : tarefas) {
            if (t.getId().equals(id)) return t.toDTO();
        }
        throw new RuntimeException("Tarefa não encontrada");
    }

    @PostMapping
    public TarefaResponse criar(@RequestBody TarefaRequest req) {
        Long proximoId = tarefas.size()+1L;
        Tarefa nova = new Tarefa(proximoId, req.titulo(),req.descricao(), req.prioridade(), StatusTarefa.PENDENTE);
        tarefas.add(nova);
        return nova.toDTO();
    }

    @PutMapping("/{id}")
    public TarefaResponse atualizar(@PathVariable Long id, @RequestBody TarefaRequest req) {
        for (Tarefa t : tarefas) {
            if (t.getId().equals(id)) {
                t.setTitulo(req.titulo());
                return t.toDTO();
            }
        }
        throw new RuntimeException("Tarefa não encontrada");
    }
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        for (int i = 0; i < tarefas.size(); i++) {
            if (tarefas.get(i).getId().equals(id)) tarefas.remove(i);
        }
    }

}
