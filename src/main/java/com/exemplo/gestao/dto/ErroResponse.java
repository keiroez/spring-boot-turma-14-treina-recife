package com.exemplo.gestao.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato unico de erro da API. Todo erro - de validacao, de regra de negocio,
 * de autenticacao ou inesperado - sai neste formato.
 *
 * O campo "campos" so aparece nos erros de validacao (um por campo invalido);
 * nos outros ele e omitido pelo @JsonInclude.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        Map<String, String> campos
) {

    public static ErroResponse de(HttpStatus status, String mensagem) {
        return new ErroResponse(LocalDateTime.now(), status.value(), mensagem, null);
    }

    public static ErroResponse validacao(Map<String, String> campos) {
        return new ErroResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), "Erro de validacao", campos);
    }
}
