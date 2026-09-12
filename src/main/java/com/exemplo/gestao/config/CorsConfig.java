package com.exemplo.gestao.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Libera CORS para a API.
 *
 * Por que isso existe: quando o frontend roda no mesmo servidor da API
 * (abrindo http://localhost:8080) o navegador nao reclama de nada. Mas se
 * o aluno abrir o HTML pelo Live Server do VS Code (http://127.0.0.1:5500)
 * e apontar o API_URL para o backend publicado no Railway, a chamada passa
 * a ser "cross-origin": o navegador bloqueia a resposta a menos que o
 * servidor autorize explicitamente, que e o que fazemos aqui.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }
}
