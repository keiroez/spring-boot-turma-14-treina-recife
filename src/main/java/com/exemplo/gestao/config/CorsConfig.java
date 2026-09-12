package com.exemplo.gestao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Libera CORS para a API.
 *
 * Por que isso existe: se o frontend for aberto de outro endereco (ex.: o Live
 * Server do VS Code em http://127.0.0.1:5500 chamando a API publicada), a
 * chamada e "cross-origin" e o navegador so entrega a resposta ao JavaScript
 * se o servidor autorizar explicitamente.
 *
 * Por que um CorsConfigurationSource e nao um WebMvcConfigurer#addCorsMappings:
 * o addCorsMappings so atua dentro do Spring MVC. Quando o Spring Security
 * recusa a requisicao (403 por falta de token), a resposta e montada na cadeia
 * de filtros, antes do MVC, e sai sem os cabecalhos CORS. O navegador bloqueia
 * essa resposta e o fetch estoura "Failed to fetch", escondendo o 403 e
 * parecendo um erro de CORS. Declarando um CorsConfigurationSource, o
 * SecurityConfig liga o filtro de CORS no inicio da cadeia e ai toda resposta
 * leva os cabecalhos - inclusive os erros.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuracao = new CorsConfiguration();
        configuracao.setAllowedOriginPatterns(List.of("*"));
        configuracao.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuracao.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource fonte = new UrlBasedCorsConfigurationSource();
        fonte.registerCorsConfiguration("/**", configuracao);
        return fonte;
    }
}
