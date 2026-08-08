package com.gestaoTarefas.app.controllers;

import com.gestaoTarefas.app.controllers.requests.TarefaRequest;
import com.gestaoTarefas.app.controllers.requests.TarefaResponse;
import com.gestaoTarefas.app.models.Tarefa;
import com.gestaoTarefas.app.models.enums.StatusTarefa;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public TarefaResponse criar(@RequestBody TarefaRequest req) {
        Long proximoId = tarefas.size()+1L;
        Tarefa nova = new Tarefa(proximoId, req.titulo(),req.descricao(), req.prioridade(), StatusTarefa.PENDENTE);
        tarefas.add(nova);
        return toResponse(nova);
    }

    @PutMapping("/{id}")
    public TarefaResponse atualizar(@PathVariable Long id, @RequestBody TarefaRequest req) {
        for (Tarefa t : tarefas) {
            if (t.getId().equals(id)) {
                t.setTitulo(req.titulo());
                return toResponse(t);
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

    public TarefaResponse toResponse(Tarefa t) {
        return new TarefaResponse(t.getId(), t.getTitulo(), t.getDescricao());
    }


}
