if (getToken()) {
    window.location.href = "/painel";
}

document.getElementById("form-cadastro").addEventListener("submit", async (e) => {
    e.preventDefault();
    limparMensagem();

    const dados = {
        nome: document.getElementById("nome").value.trim(),
        cpf: document.getElementById("cpf").value.trim(),
        email: document.getElementById("email").value.trim(),
        senha: document.getElementById("senha").value,
        dataNascimento: document.getElementById("dataNascimento").value || null,
        status: "ATIVO"
    };

    try {
        const resposta = await fetch("/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dados)
        });

        if (!resposta.ok) {
            mostrarMensagem(await extrairErro(resposta));
            return;
        }

        const token = await resposta.json();
        salvarSessao(token.token, token.email);
        window.location.href = "/painel";
    } catch (err) {
        mostrarMensagem("Não foi possível conectar ao servidor.");
    }
});
