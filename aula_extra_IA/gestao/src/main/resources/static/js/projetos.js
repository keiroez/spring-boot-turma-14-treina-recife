exigirAutenticacao();
configurarNavbar();

const form = document.getElementById("form-projeto");
const corpo = document.getElementById("tabela-corpo");
const btnCancelar = document.getElementById("btn-cancelar");
const formTitulo = document.getElementById("form-titulo");
const selResponsavel = document.getElementById("responsavelId");

// --- Carrega usuarios para o select de responsavel -------------------------
async function carregarResponsaveis() {
    const usuarios = await apiFetch("/usuarios");
    selResponsavel.innerHTML = "";
    usuarios.forEach((u) => {
        const opt = document.createElement("option");
        opt.value = u.id;
        opt.textContent = `${u.nome} (${u.email})`;
        selResponsavel.appendChild(opt);
    });
}

// --- Listagem ---------------------------------------------------------------
async function carregar() {
    const projetos = await apiFetch("/projetos");
    corpo.innerHTML = "";
    projetos.forEach((p) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${p.id}</td>
            <td>${esc(p.nome)}</td>
            <td>${esc(p.responsavelNome || "-")}</td>
            <td>${esc(p.dataInicio || "-")}</td>
            <td>${esc(p.dataConclusao || "-")}</td>
            <td><span class="badge">${esc(p.status)}</span></td>
            <td class="acoes">
                <button class="btn btn-secundario btn-sm" data-editar="${p.id}">Editar</button>
                <button class="btn btn-perigo btn-sm" data-excluir="${p.id}">Excluir</button>
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
        descricao: document.getElementById("descricao").value.trim() || null,
        dataInicio: document.getElementById("dataInicio").value,
        dataConclusao: document.getElementById("dataConclusao").value || null,
        status: document.getElementById("status").value,
        responsavelId: Number(selResponsavel.value)
    };

    try {
        if (id) {
            await apiFetch(`/projetos/${id}`, { method: "PUT", body: JSON.stringify(dados) });
            mostrarMensagem("Projeto atualizado com sucesso!", "sucesso");
        } else {
            await apiFetch("/projetos", { method: "POST", body: JSON.stringify(dados) });
            mostrarMensagem("Projeto criado com sucesso!", "sucesso");
        }
        resetarForm();
        carregar();
    } catch (err) {
        mostrarMensagem(err.message);
    }
});

// --- Editar / Excluir -------------------------------------------------------
corpo.addEventListener("click", async (e) => {
    const idEditar = e.target.getAttribute("data-editar");
    const idExcluir = e.target.getAttribute("data-excluir");

    if (idEditar) {
        const p = await apiFetch(`/projetos/${idEditar}`);
        document.getElementById("id").value = p.id;
        document.getElementById("nome").value = p.nome;
        document.getElementById("descricao").value = p.descricao || "";
        document.getElementById("dataInicio").value = p.dataInicio || "";
        document.getElementById("dataConclusao").value = p.dataConclusao || "";
        document.getElementById("status").value = p.status;
        if (p.responsavelId) selResponsavel.value = p.responsavelId;
        formTitulo.textContent = "Editar projeto #" + p.id;
        btnCancelar.classList.remove("oculto");
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    if (idExcluir && confirm("Deseja realmente excluir este projeto?")) {
        try {
            await apiFetch(`/projetos/${idExcluir}`, { method: "DELETE" });
            mostrarMensagem("Projeto excluído.", "sucesso");
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
    formTitulo.textContent = "Novo projeto";
    btnCancelar.classList.add("oculto");
}

// Inicializacao
(async function iniciar() {
    try {
        await carregarResponsaveis();
        await carregar();
    } catch (err) {
        mostrarMensagem(err.message);
    }
})();
