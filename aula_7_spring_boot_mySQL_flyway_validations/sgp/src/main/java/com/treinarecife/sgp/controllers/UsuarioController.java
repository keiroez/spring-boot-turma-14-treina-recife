package com.treinarecife.sgp.controllers;

import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.dto.UsuarioRequest;
import com.treinarecife.sgp.models.dto.UsuarioResponse;
import com.treinarecife.sgp.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<UsuarioResponse> buscarTodos(){
        List<UsuarioResponse> listaRetorno = new ArrayList<>();
        //Fui buscar todos no banco de dados
        List<Usuario> usuarios = usuarioService.buscarTodos();
        for(Usuario usuario : usuarios){
            UsuarioResponse usuarioResponse = usuario.toDTO();
            listaRetorno.add(usuarioResponse);
        }
        return listaRetorno;
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscarPorId(@PathVariable Long id){
        Usuario usuario = usuarioService.buscarPorId(id);
        return usuario.toDTO();
    }

    @PostMapping
    public UsuarioResponse inserir(@RequestBody UsuarioRequest usuarioRequest){
        Usuario usuario = usuarioService.inserir(usuarioRequest);
        return usuario.toDTO();
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody UsuarioRequest usuarioRequest){
        var usuario = usuarioService.atualizar(id, usuarioRequest);
        return usuario.toDTO();
    }

    @DeleteMapping("/{id}")
    public String deletar(@PathVariable Long id){
        usuarioService.deletar(id);
        return "Usuario deletado com sucesso!";
    }
}
