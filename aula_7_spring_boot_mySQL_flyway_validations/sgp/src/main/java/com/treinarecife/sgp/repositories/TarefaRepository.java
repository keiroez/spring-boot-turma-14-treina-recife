package com.treinarecife.sgp.repositories;

import com.treinarecife.sgp.models.Tarefa;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
}
