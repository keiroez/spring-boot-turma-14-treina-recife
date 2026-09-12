exigirAutenticacao();
configurarNavbar();

const form = document.getElementById("form-usuario");
const corpo = document.getElementById("tabela-corpo");
const btnCancelar = document.getElementById("btn-cancelar");
const formTitulo = document.getElementById("form-titulo");

// --- Listagem ---------------------------------------------------------------
async function carregar() {
    const usuarios = await apiFetch("/usuarios");
    corpo.innerHTML = "";
    usuarios.forEach((u) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${u.id}</td>
            <td>${esc(u.nome)}</td>
            <td>${esc(u.cpf)}</td>
            <td>${esc(u.email)}</td>
            <td><span class="badge">${esc(u.status)}</span></td>
            <td class="acoes">
                <button class="btn btn-secundario btn-sm" data-editar="${u.id}">Editar</button>
                <button class="btn btn-perigo btn-sm" data-excluir="${u.id}">Excluir</button>
            </td>`;
        corpo.appendChild(tr);
    });
}

// --- Criar / Atualizar ------------------------------------------------------
form.addEventListener("submit", async (e) => {
    e.preventDefault();
    limparMensagem();

    const id = document.getElementById("id").value;
    const dados = {
        nome: document.getElementById("nome").value.trim(),
        cpf: document.getElementById("cpf").value.trim(),
        email: document.getElementById("email").value.trim(),
        senha: document.getElementById("senha").value,
        dataNascimento: document.getElementById("dataNascimento").value || null,
        status: document.getElementById("status").value
    };

    try {
        if (id) {
            await apiFetch(`/usuarios/${id}`, { method: "PUT", body: JSON.stringify(dados) });
            mostrarMensagem("Usuário atualizado com sucesso!", "sucesso");
        } else {
            await apiFetch("/usuarios", { method: "POST", body: JSON.stringify(dados) });
            mostrarMensagem("Usuário criado com sucesso!", "sucesso");
        }
        resetarForm();
        carregar();
    } catch (err) {
        mostrarMensagem(err.message);
    }
});

// --- Editar / Excluir (delegacao de evento) --------------------------------
corpo.addEventListener("click", async (e) => {
    const idEditar = e.target.getAttribute("data-editar");
    const idExcluir = e.target.getAttribute("data-excluir");

    if (idEditar) {
        const u = await apiFetch(`/usuarios/${idEditar}`);
        document.getElementById("id").value = u.id;
        document.getElementById("nome").value = u.nome;
        document.getElementById("cpf").value = u.cpf;
        document.getElementById("email").value = u.email;
        document.getElementById("senha").value = "";
        document.getElementById("dataNascimento").value = u.dataNascimento || "";
        document.getElementById("status").value = u.status;
        formTitulo.textContent = "Editar usuário #" + u.id;
        btnCancelar.classList.remove("oculto");
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    if (idExcluir && confirm("Deseja realmente excluir este usuário?")) {
        try {
            await apiFetch(`/usuarios/${idExcluir}`, { method: "DELETE" });
            mostrarMensagem("Usuário excluído.", "sucesso");
            carregar();
        } catch (err) {
            mostrarMensagem(err.message);
        }
    }
});

btnCancelar.addEventListener("click", resetarForm);

function resetarForm() {
    form.reset();
    document.getElementById("id").value = "";
    formTitulo.textContent = "Novo usuário";
    btnCancelar.classList.add("oculto");
}

carregar();
