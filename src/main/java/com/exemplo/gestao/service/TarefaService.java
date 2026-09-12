package com.exemplo.gestao.service;

import com.exemplo.gestao.dto.TarefaRequest;
import com.exemplo.gestao.exception.RecursoNaoEncontradoException;
import com.exemplo.gestao.model.Projeto;
import com.exemplo.gestao.model.Tarefa;
import com.exemplo.gestao.model.Usuario;
import com.exemplo.gestao.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final ProjetoService projetoService;
    private final UsuarioService usuarioService;

    public TarefaService(TarefaRepository tarefaRepository,
                         ProjetoService projetoService,
                         UsuarioService usuarioService) {
        this.tarefaRepository = tarefaRepository;
        this.projetoService = projetoService;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<Tarefa> buscarTodos() {
        return tarefaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Tarefa buscarPorId(Long id) {
        return tarefaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Tarefa nao encontrada com id " + id));
    }

    @Transactional
    public Tarefa inserir(TarefaRequest request) {
        Tarefa tarefa = Tarefa.builder()
                .titulo(request.titulo())
                .descricao(request.descricao())
                .dataCriacao(LocalDate.now())
                .dataConclusao(request.dataConclusao())
                .prioridade(request.prioridade())
                .status(request.status())
                .projeto(resolverProjeto(request.projetoId()))
                .usuario(resolverUsuario(request.usuarioId()))
                .build();

        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public Tarefa atualizar(Long id, TarefaRequest request) {
        Tarefa tarefa = buscarPorId(id);

        tarefa.setTitulo(request.titulo());
        tarefa.setDescricao(request.descricao());
        tarefa.setDataConclusao(request.dataConclusao());
        tarefa.setPrioridade(request.prioridade());
        tarefa.setStatus(request.status());
        tarefa.setProjeto(resolverProjeto(request.projetoId()));
        tarefa.setUsuario(resolverUsuario(request.usuarioId()));

        return tarefaRepository.save(tarefa);
    }

    @Transactional
    public void deletar(Long id) {
        if (!tarefaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Tarefa nao encontrada com id " + id);
        }
        tarefaRepository.deleteById(id);
    }

    private Projeto resolverProjeto(Long projetoId) {
        return projetoId != null ? projetoService.buscarPorId(projetoId) : null;
    }

    private Usuario resolverUsuario(Long usuarioId) {
        return usuarioId != null ? usuarioService.buscarPorId(usuarioId) : null;
    }
}
