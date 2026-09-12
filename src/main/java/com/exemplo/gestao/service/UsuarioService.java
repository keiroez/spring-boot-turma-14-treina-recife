package com.exemplo.gestao.service;

import com.exemplo.gestao.dto.UsuarioRequest;
import com.exemplo.gestao.exception.RecursoNaoEncontradoException;
import com.exemplo.gestao.exception.RegraNegocioException;
import com.exemplo.gestao.model.Usuario;
import com.exemplo.gestao.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuario nao encontrado com id " + id));
    }

    @Transactional
    public Usuario inserir(UsuarioRequest request) {
        validarUnicidade(request.email(), request.cpf());

        Usuario usuario = Usuario.builder()
                .nome(request.nome())
                .cpf(request.cpf())
                .email(request.email())
                .senha(passwordEncoder.encode(request.senha()))
                .dataNascimento(request.dataNascimento())
                .status(request.status())
                .build();

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Usuario atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = buscarPorId(id);

        usuario.setNome(request.nome());
        usuario.setCpf(request.cpf());
        usuario.setEmail(request.email());
        // So re-encoda a senha se uma nova foi informada
        if (request.senha() != null && !request.senha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(request.senha()));
        }
        usuario.setDataNascimento(request.dataNascimento());
        usuario.setStatus(request.status());

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Usuario nao encontrado com id " + id);
        }
        usuarioRepository.deleteById(id);
    }

    private void validarUnicidade(String email, String cpf) {
        if (usuarioRepository.existsByEmail(email)) {
            throw new RegraNegocioException("Ja existe um usuario com o e-mail " + email);
        }
        if (usuarioRepository.existsByCpf(cpf)) {
            throw new RegraNegocioException("Ja existe um usuario com o CPF " + cpf);
        }
    }
}
