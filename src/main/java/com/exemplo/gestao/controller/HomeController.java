package com.exemplo.gestao.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Unica rota que nao faz parte da API: manda quem abre a raiz do dominio
 * para a documentacao, em vez de devolver um erro. Sem isso, acessar
 * https://seu-projeto.up.railway.app/ pareceria que o deploy quebrou.
 */
@RestController
public class HomeController {

    @GetMapping("/")
    public RedirectView raiz() {
        return new RedirectView("/swagger-ui.html");
    }
}
