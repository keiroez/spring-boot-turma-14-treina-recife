package com.exemplo.gestao.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller MVC que serve as paginas Thymeleaf (frontend).
 * As rotas ficam sob /painel/** para nao conflitar com os
 * endpoints REST (/usuarios, /projetos, /tarefas).
 */
@Controller
public class WebController {

    @GetMapping("/")
    public String raiz() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String cadastro() {
        return "cadastro";
    }

    @GetMapping("/painel")
    public String painel() {
        return "painel";
    }

    @GetMapping("/painel/usuarios")
    public String usuarios() {
        return "usuarios";
    }

    @GetMapping("/painel/projetos")
    public String projetos() {
        return "projetos";
    }

    @GetMapping("/painel/tarefas")
    public String tarefas() {
        return "tarefas";
    }
}
