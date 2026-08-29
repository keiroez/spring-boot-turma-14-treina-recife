package com.treinarecife.sgp.services;

import com.treinarecife.sgp.models.Projeto;
import com.treinarecife.sgp.models.Usuario;
import com.treinarecife.sgp.models.dto.ProjetoRequest;
import com.treinarecife.sgp.repositories.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Projeto> buscarTodos(){
        return projetoRepository.findAll();
    }

    public Projeto buscarPorId(long id){
        return projetoRepository.getReferenceById(id);
    }

    public Projeto inserir(ProjetoRequest projetoRequest){
        Usuario usuario = usuarioService.buscarPorId(projetoRequest.idUsuario());
        Projeto projeto = new Projeto(
                projetoRequest.nome(),
                projetoRequest.descricao(),
                projetoRequest.dataInicio(),
                projetoRequest.dataConclusao(),
                projetoRequest.status(),
                usuario
        );
        return projetoRepository.save(projeto);
    }

    public Projeto atualizar(Long id, ProjetoRequest projetoRequest){
        Projeto projeto = projetoRepository.getReferenceById(id);
        projeto.setNome(projetoRequest.nome());
        projeto.setDescricao(projetoRequest.descricao());
        projeto.setDataInicio(projetoRequest.dataInicio());
        projeto.setDataConclusao(projetoRequest.dataConclusao());
        projeto.setStatus(projetoRequest.status());
        Usuario usuario = usuarioService.buscarPorId(projetoRequest.idUsuario());
        projeto.setResponsavel(usuario);
        return projetoRepository.save(projeto);
    }

    public void deletar(Long id){
        projetoRepository.deleteById(id);
    }
}
