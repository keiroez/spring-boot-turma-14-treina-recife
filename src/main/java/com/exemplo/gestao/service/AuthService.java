package com.exemplo.gestao.service;

import com.exemplo.gestao.dto.LoginRequest;
import com.exemplo.gestao.dto.TokenResponse;
import com.exemplo.gestao.dto.UsuarioRequest;
import com.exemplo.gestao.model.Usuario;
import com.exemplo.gestao.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioService usuarioService;

    public AuthService(AuthenticationManager authenticationManager,
                       TokenService tokenService,
                       UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
        this.usuarioService = usuarioService;
    }

    /**
     * Autentica as credenciais e devolve um token JWT.
     */
    public TokenResponse login(LoginRequest request) {
        var credenciais = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication autenticacao = authenticationManager.authenticate(credenciais);

        Usuario usuario = (Usuario) autenticacao.getPrincipal();
        String token = tokenService.gerarToken(usuario);

        return new TokenResponse(token, usuario.getEmail());
    }

    /**
     * Registra um novo usuario e ja devolve um token JWT valido.
     */
    @Transactional
    public TokenResponse register(UsuarioRequest request) {
        Usuario usuario = usuarioService.inserir(request);
        String token = tokenService.gerarToken(usuario);
        return new TokenResponse(token, usuario.getEmail());
    }
}
