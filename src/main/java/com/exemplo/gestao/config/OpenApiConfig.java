package com.exemplo.gestao.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String SECURITY_SCHEME = "bearer-jwt";

    @Bean
    public OpenAPI gestaoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestao de Projetos")
                        .description("CRUD de Usuarios, Projetos e Tarefas com autenticacao JWT stateless")
                        .version("1.0.0"))
                // Apenas declara o esquema de seguranca. Quem exige o token sao os
                // controllers, via @SecurityRequirement -> as rotas publicas ficam sem cadeado.
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME,
                        new SecurityScheme()
                                .name(SECURITY_SCHEME)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")));
    }

    // ------------------------------------------------------------------
    // Dois grupos no Swagger UI (o seletor fica no topo da pagina).
    // O seletor ordena os grupos pelo displayName, por isso os nomes
    // comecam com "1." e "2.": garante o grupo publico primeiro.
    // ------------------------------------------------------------------

    @Bean
    public GroupedOpenApi rotasPublicas() {
        return GroupedOpenApi.builder()
                .group("1-publico")
                .displayName("1. Rotas publicas (sem token)")
                .pathsToMatch("/auth/**", "/public/**")
                // Os controllers do CRUD sao anotados com @SecurityRequirement, o que
                // desenha um cadeado em todas as operacoes deles. Aqui, neste grupo,
                // limpamos essa exigencia: nenhuma rota /public/** pede token.
                .addOpenApiCustomizer(api -> api.getPaths().values()
                        .forEach(rota -> rota.readOperations()
                                .forEach(operacao -> operacao.setSecurity(null))))
                .build();
    }

    @Bean
    public GroupedOpenApi rotasPrivadas() {
        return GroupedOpenApi.builder()
                .group("2-privado")
                .displayName("2. Rotas privadas (exigem JWT)")
                // /auth/login aparece aqui tambem para pegar o token e clicar
                // em "Authorize" sem precisar trocar de grupo.
                .pathsToMatch("/auth/login", "/usuarios/**", "/projetos/**", "/tarefas/**")
                .build();
    }
}
