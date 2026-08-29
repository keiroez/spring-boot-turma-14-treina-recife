package com.exemplo.gestao.controller;

import com.exemplo.gestao.dto.TarefaRequest;
import com.exemplo.gestao.dto.TarefaResponse;
import com.exemplo.gestao.service.TarefaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService tarefaService;

    public TarefaController(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @GetMapping
    public List<TarefaResponse> buscarTodos() {
        return tarefaService.buscarTodos()
                .stream()
                .map(tarefa -> tarefa.toDTO())
                .toList();
    }

    @GetMapping("/{id}")
    public TarefaResponse buscarPorId(@PathVariable Long id) {
        return tarefaService.buscarPorId(id).toDTO();
    }

    @PostMapping
    public ResponseEntity<TarefaResponse> inserir(@Valid @RequestBody TarefaRequest request) {
        TarefaResponse response = tarefaService.inserir(request).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public TarefaResponse atualizar(@PathVariable Long id, @Valid @RequestBody TarefaRequest request) {
        return tarefaService.atualizar(id, request).toDTO();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tarefaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
