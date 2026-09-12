// Se ja estiver logado, vai direto ao painel.
if (getToken()) {
    window.location.href = "painel.html";
}

document.getElementById("form-login").addEventListener("submit", async (e) => {
    e.preventDefault();
    limparMensagem();

    const dados = {
        email: document.getElementById("email").value.trim(),
        senha: document.getElementById("senha").value
    };

    try {
        const resposta = await fetch(API_URL + "/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dados)
        });

        if (!resposta.ok) {
            const erro = await extrairErro(resposta);
            mostrarMensagem(erro);
            return;
        }

        const token = await resposta.json();
        salvarSessao(token.token, token.email);
        window.location.href = "painel.html";
    } catch (err) {
        mostrarMensagem("Não foi possível conectar ao servidor.");
    }
});
