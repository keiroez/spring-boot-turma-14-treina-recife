package com.exemplo.gestao.controller;

import com.exemplo.gestao.dto.UsuarioRequest;
import com.exemplo.gestao.dto.UsuarioResponse;
import com.exemplo.gestao.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> buscarTodos() {
        return usuarioService.buscarTodos()
                .stream()
                .map(usuario -> usuario.toDTO())
                .toList();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id) {
        return usuarioService.buscarPorId(id).toDTO();
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> inserir(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = usuarioService.inserir(request).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return usuarioService.atualizar(id, request).toDTO();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
