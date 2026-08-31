exigirAutenticacao();
configurarNavbar();

const form = document.getElementById("form-tarefa");
const corpo = document.getElementById("tabela-corpo");
const btnCancelar = document.getElementById("btn-cancelar");
const formTitulo = document.getElementById("form-titulo");
const selProjeto = document.getElementById("projetoId");
const selUsuario = document.getElementById("usuarioId");

// Mapas id -> nome para exibir na tabela
let nomesProjetos = {};
let nomesUsuarios = {};

// --- Carrega selects de projeto e responsavel ------------------------------
async function carregarSelects() {
    const [projetos, usuarios] = await Promise.all([
        apiFetch("/projetos"),
        apiFetch("/usuarios")
    ]);

    selProjeto.innerHTML = '<option value="">— nenhum —</option>';
    nomesProjetos = {};
    projetos.forEach((p) => {
        nomesProjetos[p.id] = p.nome;
        const opt = document.createElement("option");
        opt.value = p.id;
        opt.textContent = p.nome;
        selProjeto.appendChild(opt);
    });

    selUsuario.innerHTML = '<option value="">— nenhum —</option>';
    nomesUsuarios = {};
    usuarios.forEach((u) => {
        nomesUsuarios[u.id] = u.nome;
        const opt = document.createElement("option");
        opt.value = u.id;
        opt.textContent = u.nome;
        selUsuario.appendChild(opt);
    });
}

// --- Listagem ---------------------------------------------------------------
async function carregar() {
    const tarefas = await apiFetch("/tarefas");
    corpo.innerHTML = "";
    tarefas.forEach((t) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${t.id}</td>
            <td>${esc(t.titulo)}</td>
            <td><span class="badge prioridade-${esc(t.prioridade)}">${esc(t.prioridade)}</span></td>
            <td><span class="badge">${esc(t.status)}</span></td>
            <td>${esc(nomesProjetos[t.projetoId] || "-")}</td>
            <td>${esc(nomesUsuarios[t.usuarioId] || "-")}</td>
            <td class="acoes">
                <button class="btn btn-secundario btn-sm" data-editar="${t.id}">Editar</button>
                <button class="btn btn-perigo btn-sm" data-excluir="${t.id}">Excluir</button>
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
        titulo: document.getElementById("titulo").value.trim(),
        descricao: document.getElementById("descricao").value.trim() || null,
        dataConclusao: document.getElementById("dataConclusao").value || null,
        prioridade: document.getElementById("prioridade").value,
        status: document.getElementById("status").value,
        projetoId: selProjeto.value ? Number(selProjeto.value) : null,
        usuarioId: selUsuario.value ? Number(selUsuario.value) : null
    };

    try {
        if (id) {
            await apiFetch(`/tarefas/${id}`, { method: "PUT", body: JSON.stringify(dados) });
            mostrarMensagem("Tarefa atualizada com sucesso!", "sucesso");
        } else {
            await apiFetch("/tarefas", { method: "POST", body: JSON.stringify(dados) });
            mostrarMensagem("Tarefa criada com sucesso!", "sucesso");
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
        const t = await apiFetch(`/tarefas/${idEditar}`);
        document.getElementById("id").value = t.id;
        document.getElementById("titulo").value = t.titulo;
        document.getElementById("descricao").value = t.descricao || "";
        document.getElementById("dataConclusao").value = t.dataConclusao || "";
        document.getElementById("prioridade").value = t.prioridade;
        document.getElementById("status").value = t.status;
        selProjeto.value = t.projetoId || "";
        selUsuario.value = t.usuarioId || "";
        formTitulo.textContent = "Editar tarefa #" + t.id;
        btnCancelar.classList.remove("oculto");
        window.scrollTo({ top: 0, behavior: "smooth" });
    }

    if (idExcluir && confirm("Deseja realmente excluir esta tarefa?")) {
        try {
            await apiFetch(`/tarefas/${idExcluir}`, { method: "DELETE" });
            mostrarMensagem("Tarefa excluída.", "sucesso");
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
    formTitulo.textContent = "Nova tarefa";
    btnCancelar.classList.add("oculto");
}

// Inicializacao
(async function iniciar() {
    try {
        await carregarSelects();
        await carregar();
    } catch (err) {
        mostrarMensagem(err.message);
    }
})();
