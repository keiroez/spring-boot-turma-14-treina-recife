// ============================================================
// api.js - utilitarios compartilhados de autenticacao e acesso
// a API REST protegida por JWT.
// ============================================================

const CHAVE_TOKEN = "gestao_token";
const CHAVE_EMAIL = "gestao_email";

// --- Sessao (localStorage) --------------------------------------------------
function getToken() {
    return localStorage.getItem(CHAVE_TOKEN);
}

function salvarSessao(token, email) {
    localStorage.setItem(CHAVE_TOKEN, token);
    if (email) localStorage.setItem(CHAVE_EMAIL, email);
}

function limparSessao() {
    localStorage.removeItem(CHAVE_TOKEN);
    localStorage.removeItem(CHAVE_EMAIL);
}

// Redireciona para o login caso nao exista token. Usar no topo das paginas protegidas.
function exigirAutenticacao() {
    if (!getToken()) {
        window.location.href = "/login";
    }
}

// --- Chamada autenticada ----------------------------------------------------
// Envia o token Bearer e trata JSON. Em 401/403 encerra a sessao e volta ao login.
async function apiFetch(url, options = {}) {
    const headers = Object.assign(
        { "Content-Type": "application/json" },
        options.headers || {}
    );
    const token = getToken();
    if (token) {
        headers["Authorization"] = "Bearer " + token;
    }

    const resposta = await fetch(url, { ...options, headers });

    if (resposta.status === 401 || resposta.status === 403) {
        limparSessao();
        window.location.href = "/login";
        throw new Error("Sessão expirada. Faça login novamente.");
    }

    if (!resposta.ok) {
        const erro = await extrairErro(resposta);
        throw new Error(erro);
    }

    // 204 No Content nao tem corpo
    if (resposta.status === 204) return null;
    return resposta.json();
}

// Extrai a mensagem de erro do padrao devolvido pelo GlobalExceptionHandler.
async function extrairErro(resposta) {
    try {
        const corpo = await resposta.json();
        if (corpo.erro) return corpo.erro;
        if (corpo.campos) {
            return Object.values(corpo.campos).join(" | ");
        }
        return JSON.stringify(corpo);
    } catch (e) {
        return "Erro " + resposta.status;
    }
}

// --- UI helpers -------------------------------------------------------------
function mostrarMensagem(texto, tipo = "erro") {
    const el = document.getElementById("mensagem");
    if (!el) return;
    el.textContent = texto;
    el.className = "mensagem " + tipo;
}

function limparMensagem() {
    mostrarMensagem("", "");
}

// Escapa texto para insercao segura via innerHTML.
function esc(valor) {
    if (valor === null || valor === undefined) return "";
    return String(valor)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#39;");
}

// Configura o botao "Sair" e exibe o e-mail logado (chamar nas paginas com navbar).
function configurarNavbar() {
    const emailEl = document.getElementById("usuario-logado");
    if (emailEl) {
        emailEl.textContent = localStorage.getItem(CHAVE_EMAIL) || "";
    }
    const btn = document.getElementById("btn-logout");
    if (btn) {
        btn.addEventListener("click", () => {
            limparSessao();
            window.location.href = "/login";
        });
    }
}
