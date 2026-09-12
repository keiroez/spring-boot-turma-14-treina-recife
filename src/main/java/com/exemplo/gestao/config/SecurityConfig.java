package com.exemplo.gestao.config;

import com.exemplo.gestao.security.ErroSegurancaHandler;
import com.exemplo.gestao.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private static final String[] ENDPOINTS_PUBLICOS = {
            // Raiz: redireciona para a documentacao
            "/",
            "/auth/login",
            "/auth/register",
            // Copia aberta do CRUD (/public/usuarios, /public/projetos, /public/tarefas).
            // Serve para comecar o frontend sem se preocupar com token.
            "/public/**",
            // Documentacao OpenAPI / Swagger
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    private final JwtFilter jwtFilter;
    private final ErroSegurancaHandler erroSegurancaHandler;

    public SecurityConfig(JwtFilter jwtFilter, ErroSegurancaHandler erroSegurancaHandler) {
        this.jwtFilter = jwtFilter;
        this.erroSegurancaHandler = erroSegurancaHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // API stateless com JWT -> CSRF nao se aplica
                .csrf(AbstractHttpConfigurer::disable)
                // Usa o CorsConfigurationSource do CorsConfig. Estando na cadeia de
                // filtros, ate as respostas 401/403 saem com os cabecalhos CORS.
                .cors(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ENDPOINTS_PUBLICOS).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                // Sem isto, uma rota privada sem token responde 401/403 com corpo
                // vazio. O handler devolve o mesmo JSON de erro do resto da API.
                .exceptionHandling(erros -> erros
                        .authenticationEntryPoint(erroSegurancaHandler)
                        .accessDeniedHandler(erroSegurancaHandler)
                )
                // Adiciona o filtro JWT antes do filtro padrao de usuario/senha
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}
