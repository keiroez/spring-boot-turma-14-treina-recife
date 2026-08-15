package com.treinarecife.sgp.services;

import com.treinarecife.sgp.models.Projeto;
import com.treinarecife.sgp.repositories.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjetoService {
    @Autowired
    private ProjetoRepository projetoRepository;

    public void inserir(Projeto novoProjeto){
        projetoRepository.save(novoProjeto);
    }

    public List<Projeto> buscarTodos(){
        return projetoRepository.findAll();
    }

    public Projeto buscarPorId(Long id){
        return projetoRepository.getReferenceById(id);
    }

    public void atualizar(Projeto novoProjeto){
        projetoRepository.save(novoProjeto);
    }

    public void deletar(Long id){
        projetoRepository.deleteById(id);
    }
}
