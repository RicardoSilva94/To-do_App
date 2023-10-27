// Definição da URL do endpoint da API de tarefas
const tasksEndpoint = "http://localhost:8080/task/user";

// Função para ocultar o loader (indicador de carregamento)
function hideLoader() {
    document.getElementById("loading").style.display = "none";
}

// Função para exibir as tarefas na página
function show(tasks) {
    // Inicializa a tabela HTML com cabeçalhos
    let tab = `<thead>
            <th scope="col">#</th>
            <th scope="col">Description</th>
        </thead>`;
    // Itera sobre as tarefas e adiciona linhas à tabela
    for (let task of tasks) {
        tab += `
            <tr>
                <td scope="row">${task.id}</td>
                <td>${task.description}</td>
            </tr>
        `;
    }
    // Define o conteúdo da tabela no elemento com o ID "tasks"
    document.getElementById("tasks").innerHTML = tab;
}
// Função assíncrona para obter as tasks da API
async function getTasks() {
    // Define a chave de autorização como "Authorization"
    let key = "Authorization";
    // Realiza uma solicitação GET para a URL do endpoint de tasks
    const response = await fetch(tasksEndpoint, {
        method: "GET",
        // Define o cabeçalho de autorização com o token de acesso armazenado localmente
        headers: new Headers({
            Authorization: localStorage.getItem(key),
        }),
    });
    // Analisa a resposta como JSON
    var data = await response.json();
    console.log(data);
    // Se houver resposta (status 200 OK), oculta o loader e exibe as tarefas
    if (response) hideLoader();
    show(data);
}
// Aguarda o evento "DOMContentLoaded" antes de executar o código
document.addEventListener("DOMContentLoaded", function (event) {
    // Redireciona para a página de login se não houver um token de autorização armazenado localmente
    if (!localStorage.getItem("Authorization"))
        window.location = "/view/login.html";
});
// Chama a função getTasks para carregar as tasks quando a página é carregada
getTasks();
