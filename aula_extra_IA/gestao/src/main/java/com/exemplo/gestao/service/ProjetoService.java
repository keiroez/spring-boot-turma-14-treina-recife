package com.exemplo.gestao.service;

import com.exemplo.gestao.dto.ProjetoRequest;
import com.exemplo.gestao.exception.RecursoNaoEncontradoException;
import com.exemplo.gestao.model.Projeto;
import com.exemplo.gestao.model.Usuario;
import com.exemplo.gestao.repository.ProjetoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final UsuarioService usuarioService;

    public ProjetoService(ProjetoRepository projetoRepository, UsuarioService usuarioService) {
        this.projetoRepository = projetoRepository;
        this.usuarioService = usuarioService;
    }

    @Transactional(readOnly = true)
    public List<Projeto> buscarTodos() {
        return projetoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Projeto buscarPorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Projeto nao encontrado com id " + id));
    }

    @Transactional
    public Projeto inserir(ProjetoRequest request) {
        Usuario responsavel = usuarioService.buscarPorId(request.responsavelId());

        Projeto projeto = Projeto.builder()
                .nome(request.nome())
                .descricao(request.descricao())
                .dataInicio(request.dataInicio())
                .dataConclusao(request.dataConclusao())
                .status(request.status())
                .responsavel(responsavel)
                .build();

        return projetoRepository.save(projeto);
    }

    @Transactional
    public Projeto atualizar(Long id, ProjetoRequest request) {
        Projeto projeto = buscarPorId(id);
        Usuario responsavel = usuarioService.buscarPorId(request.responsavelId());

        projeto.setNome(request.nome());
        projeto.setDescricao(request.descricao());
        projeto.setDataInicio(request.dataInicio());
        projeto.setDataConclusao(request.dataConclusao());
        projeto.setStatus(request.status());
        projeto.setResponsavel(responsavel);

        return projetoRepository.save(projeto);
    }

    @Transactional
    public void deletar(Long id) {
        if (!projetoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Projeto nao encontrado com id " + id);
        }
        projetoRepository.deleteById(id);
    }
}
