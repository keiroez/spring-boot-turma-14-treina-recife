package com.exemplo.gestao.controller;

import com.exemplo.gestao.dto.ProjetoRequest;
import com.exemplo.gestao.dto.ProjetoResponse;
import com.exemplo.gestao.service.ProjetoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    private final ProjetoService projetoService;

    public ProjetoController(ProjetoService projetoService) {
        this.projetoService = projetoService;
    }

    @GetMapping
    public List<ProjetoResponse> buscarTodos() {
        return projetoService.buscarTodos()
                .stream()
                .map(projeto -> projeto.toDTO())
                .toList();
    }

    @GetMapping("/{id}")
    public ProjetoResponse buscarPorId(@PathVariable Long id) {
        return projetoService.buscarPorId(id).toDTO();
    }

    @PostMapping
    public ResponseEntity<ProjetoResponse> inserir(@Valid @RequestBody ProjetoRequest request) {
        ProjetoResponse response = projetoService.inserir(request).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ProjetoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProjetoRequest request) {
        return projetoService.atualizar(id, request).toDTO();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        projetoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
