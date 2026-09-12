package com.exemplo.gestao.exception;

import com.exemplo.gestao.dto.ErroResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tools.jackson.databind.exc.InvalidFormatException;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Converte qualquer excecao em uma resposta ErroResponse.
 *
 * Os erros barrados pelo Spring Security (401 e 403) nao passam por aqui:
 * eles acontecem na cadeia de filtros, antes do Spring MVC. Quem os formata
 * no mesmo JSON e o ErroSegurancaHandler.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ------------------------------------------------------------------
    // Erros de negocio (lancados pelos services)
    // ------------------------------------------------------------------

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return montar(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<ErroResponse> tratarRegraNegocio(RegraNegocioException ex) {
        return montar(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // ------------------------------------------------------------------
    // Autenticacao no /auth/login
    // ------------------------------------------------------------------

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErroResponse> tratarCredenciaisInvalidas(BadCredentialsException ex) {
        return montar(HttpStatus.UNAUTHORIZED, "E-mail ou senha invalidos");
    }

    /** Usuario INATIVO ou BLOQUEADO, por exemplo. */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErroResponse> tratarAutenticacao(AuthenticationException ex) {
        return montar(HttpStatus.UNAUTHORIZED, "Nao foi possivel autenticar: usuario inativo ou bloqueado");
    }

    // ------------------------------------------------------------------
    // Requisicao mal formada
    // ------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> tratarValidacao(MethodArgumentNotValidException ex) {
        Map<String, String> campos = new LinkedHashMap<>();
        for (FieldError erro : ex.getBindingResult().getFieldErrors()) {
            campos.put(erro.getField(), erro.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(ErroResponse.validacao(campos));
    }

    /**
     * JSON invalido, data em formato errado ou valor fora de um enum.
     * No caso do enum a mensagem lista os valores aceitos, que e o erro mais
     * comum de quem esta consumindo a API pela primeira vez.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> tratarCorpoInvalido(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof InvalidFormatException causa) {
            Class<?> tipo = causa.getTargetType();
            String valoresAceitos = tipo.isEnum()
                    ? " Valores aceitos: " + Arrays.stream(tipo.getEnumConstants())
                            .map(Object::toString)
                            .collect(Collectors.joining(", "))
                    : "";
            return montar(HttpStatus.BAD_REQUEST, "Valor invalido para o campo "
                    + nomeDoCampo(causa) + ": " + causa.getValue() + "." + valoresAceitos);
        }
        return montar(HttpStatus.BAD_REQUEST, "Corpo da requisicao invalido: confira se o JSON esta bem formado");
    }

    /** Ex.: GET /tarefas/abc, com texto onde se espera um numero. */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> tratarTipoInvalido(MethodArgumentTypeMismatchException ex) {
        return montar(HttpStatus.BAD_REQUEST,
                "Parametro " + ex.getName() + " invalido: " + ex.getValue());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> tratarMetodoNaoSuportado(HttpRequestMethodNotSupportedException ex) {
        return montar(HttpStatus.METHOD_NOT_ALLOWED,
                "Metodo " + ex.getMethod() + " nao permitido nesta rota");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResponse> tratarMediaTypeNaoSuportado(HttpMediaTypeNotSupportedException ex) {
        return montar(HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                "Envie o cabecalho Content-Type: application/json");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> tratarRotaInexistente(NoResourceFoundException ex) {
        return montar(HttpStatus.NOT_FOUND, "Rota nao encontrada: /" + ex.getResourcePath());
    }

    // ------------------------------------------------------------------
    // Banco e rede de seguranca
    // ------------------------------------------------------------------

    /** Ex.: excluir um usuario que ainda e responsavel por um projeto. */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> tratarIntegridade(DataIntegrityViolationException ex) {
        log.warn("Violacao de integridade: {}", ex.getMostSpecificCause().getMessage());
        return montar(HttpStatus.CONFLICT,
                "Operacao nao permitida: o registro esta em uso ou viola uma restricao do banco");
    }

    /** Ultima rede: qualquer excecao nao prevista tambem sai no formato padrao. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> tratarInesperado(Exception ex) {
        log.error("Erro inesperado", ex);
        return montar(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor");
    }

    // ------------------------------------------------------------------

    private ResponseEntity<ErroResponse> montar(HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(ErroResponse.de(status, mensagem));
    }

    private String nomeDoCampo(InvalidFormatException causa) {
        if (causa.getPath().isEmpty()) {
            return "informado";
        }
        return causa.getPath().get(causa.getPath().size() - 1).getPropertyName();
    }
}
