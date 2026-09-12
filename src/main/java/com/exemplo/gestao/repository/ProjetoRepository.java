package com.exemplo.gestao.repository;

import com.exemplo.gestao.model.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
}
