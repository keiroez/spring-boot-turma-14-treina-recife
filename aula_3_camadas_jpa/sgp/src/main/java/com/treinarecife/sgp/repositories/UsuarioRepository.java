package com.treinarecife.sgp.repositories;

import com.treinarecife.sgp.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
}
