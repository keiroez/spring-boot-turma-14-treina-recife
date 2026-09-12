package com.exemplo.gestao.security;

import com.exemplo.gestao.dto.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * O GlobalExceptionHandler nao alcanca os erros do Spring Security: eles
 * acontecem na cadeia de filtros, antes do Spring MVC. Esta classe fecha essa
 * lacuna escrevendo o mesmo ErroResponse na resposta, para a API nunca
 * devolver corpo vazio.
 *
 * - commence: nao ha usuario autenticado  -> 401
 * - handle:   ha usuario, mas sem permissao -> 403
 */
@Component
public class ErroSegurancaHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public ErroSegurancaHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException excecao) throws IOException {
        escrever(response, HttpStatus.UNAUTHORIZED,
                "Token ausente, invalido ou expirado. Faca login em POST /auth/login e envie o "
                        + "cabecalho Authorization: Bearer SEU_TOKEN");
    }

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException excecao) throws IOException {
        escrever(response, HttpStatus.FORBIDDEN, "Acesso negado para este recurso");
    }

    private void escrever(HttpServletResponse response, HttpStatus status, String mensagem) throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), ErroResponse.de(status, mensagem));
    }
}
