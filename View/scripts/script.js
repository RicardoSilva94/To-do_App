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
            <th scope="col">Actions</th>
        </thead>`;
    // Itera sobre as tarefas e adiciona linhas à tabela
    for (let task of tasks) {
        tab += `
            <tr>
                <td scope="row">${task.id}</td>
                <td>${task.description}</td>
                <td><button class="btn btn-danger" onclick="deleteTask(${task.id})">Delete</button>
                <button class="btn btn-primary" onclick="editTaskForm(${task.id}, '${task.description}')">Edit</button>
                </td>
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



// Função para exibir o formulário de criação de tarefa
function showNewTaskForm() {
    document.getElementById("newTaskForm").style.display = "block"; // Exibe o formulário
    document.getElementById("newTaskButton").style.display = "none"; // Oculta o botão "New Task"
}

// Função para ocultar o formulário de criação de tarefa
function hideNewTaskForm() {
    document.getElementById("newTaskForm").style.display = "none"; // Oculta o formulário
    document.getElementById("newTaskButton").style.display = "block"; // Exibe o botão "New Task"
}

// Função para adicionar uma nova tarefa
async function addTask() {

    console.log("Tentando adicionar a tarefa");
    const taskDescription = document.getElementById("taskDescription").value;

    const key = "Authorization";
    const token = localStorage.getItem(key);

    const response = await fetch("http://localhost:8080/task", {
        method: "POST",
        headers: new Headers({
            "Content-Type": "application/json",
            Authorization: token,
        }),
        body: JSON.stringify({
            description: taskDescription,
        }),
    });

    if (response.ok) {
        // Recarrega a lista de tarefas após adicionar uma nova tarefa
        getTasks();
        // Oculta o formulário de criação de tarefa
        hideNewTaskForm();
    } else {
        // Trate erros de adição de tarefa aqui (por exemplo, exibindo uma mensagem de erro)
        console.error("Erro ao adicionar tarefa");
    }
}

// Aguarda o evento "DOMContentLoaded" antes de executar o código
document.addEventListener("DOMContentLoaded", function (event) {
    // Redireciona para a página de login se não houver um token de autorização armazenado localmente
    if (!localStorage.getItem("Authorization"))
        window.location = "/view/login.html";
});

// Chama a função getTasks para carregar as tarefas quando a página é carregada
getTasks();


// Função para eliminar uma tarefa
async function deleteTask(taskId) {
    const key = "Authorization";
    const token = localStorage.getItem(key);

    try {
        const response = await fetch(`http://localhost:8080/task/${taskId}`, {
            method: "DELETE",
            headers: new Headers({
                Authorization: token,
            }),
        });

        if (response.ok) {
            // Encontra a linha correspondente ao ID da tarefa e remove
            const rows = document.querySelectorAll("#tasks tr");
            rows.forEach(row => {
                const cell = row.querySelector("td:first-child");
                if (cell && cell.textContent === String(taskId)) {
                    row.parentNode.removeChild(row);
                }
            });
        } else {
            const errorMessage = await response.text(); // Captura a mensagem de erro da resposta
            console.error("Erro ao excluir a tarefa:", errorMessage);
        }
    } catch (error) {
        console.error("Erro ao excluir a tarefa:", error);
    }
}


// Função para editar uma tarefa
async function editTask(taskId, newDescription) {
    const key = "Authorization";
    const token = localStorage.getItem(key);

    try {
        const response = await fetch(`http://localhost:8080/task/${taskId}`, {
            method: "PUT",
            headers: new Headers({
                "Content-Type": "application/json",
                Authorization: token,
            }),
            body: JSON.stringify({
                id: taskId,
                description: newDescription,
                // Adicione outros campos que deseja atualizar na tarefa, se houver
            }),
        });

        if (response.ok) {
            // Atualize a lista de tarefas após a edição bem-sucedida da tarefa
            getTasks();
            // Oculta o formulário de edição de tarefa
            hideEditTaskForm();
        } else {
            const errorMessage = await response.text(); // Captura a mensagem de erro da resposta
            console.error("Erro ao editar a tarefa:", errorMessage);
        }
    } catch (error) {
        console.error("Erro ao editar a tarefa:", error);
    }
}

function editTaskForm(taskId, currentDescription) {
    const rows = document.querySelectorAll("#tasks tr");
    for (let row of rows) {
        const cells = row.querySelectorAll("td");
        for (let cell of cells) {
            if (cell.textContent.trim() === String(taskId)) {
                const actionsCell = row.querySelector("td:last-child");
                const form = document.createElement('form');
                form.onsubmit = function() {
                    saveEditedTask(taskId);
                    return false;
                };
                const label = document.createElement('label');
                label.for = "newDescription";
                label.textContent = "New Description:";
                const input = document.createElement('input');
                input.type = "text";
                input.id = "newDescription";
                input.value = currentDescription;
                input.required = true;
                const button = document.createElement('button');
                button.type = "submit";
                button.className = "btn btn-success";
                button.textContent = "Save";
                form.appendChild(label);
                form.appendChild(input);
                form.appendChild(button);
                actionsCell.innerHTML = '';
                actionsCell.appendChild(form);
                break;
            }
        }
    }
}


async function saveEditedTask(taskId) {
    const newDescription = document.getElementById("newDescription").value;
    await editTask(taskId, newDescription);
}