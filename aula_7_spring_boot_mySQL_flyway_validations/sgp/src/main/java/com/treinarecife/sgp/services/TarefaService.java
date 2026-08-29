package com.treinarecife.sgp.services;

import com.treinarecife.sgp.models.Tarefa;
import com.treinarecife.sgp.models.dto.TarefaRequest;
import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.repositories.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {
    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Tarefa> buscarTodos(){
        return tarefaRepository.findAll();
    }

    public Tarefa buscarPorId(long id){
        return tarefaRepository.getReferenceById(id);
    }

    public Tarefa inserir(TarefaRequest tarefaRequest){
        Usuario usuario = usuarioService.buscarPorId(tarefaRequest.usuario());
        Tarefa tarefa = new Tarefa(
                tarefaRequest.titulo(),
                tarefaRequest.descricao(),
                tarefaRequest.dataCriacao(),
                tarefaRequest.dataConclusao(),
                tarefaRequest.prioridade(),
                tarefaRequest.status(),
                usuario
        );
        return tarefaRepository.save(tarefa);
    }

    public Tarefa atualizar(Long id, TarefaRequest tarefaRequest){
        Tarefa tarefa = tarefaRepository.getReferenceById(id);
        tarefa.setTitulo(tarefaRequest.titulo());
        tarefa.setDescricao(tarefaRequest.descricao());
        tarefa.setDataCriacao(tarefaRequest.dataCriacao());
        tarefa.setDataConclusao(tarefaRequest.dataConclusao());
        tarefa.setStatus(tarefaRequest.status());
        Usuario usuario = usuarioService.buscarPorId(tarefaRequest.usuario());
        tarefa.setUsuario(usuario);
        return tarefaRepository.save(tarefa);
    }

    public void deletar(Long id){
        tarefaRepository.deleteById(id);
    }
}
