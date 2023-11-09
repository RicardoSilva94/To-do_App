async function signup() {
    // Obtém os valores de username e password a partir dos elementos de input no file HTML.
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    // Exibe os valores no console para fins de depuração
    console.log(username, password);

    // Envia uma requisição POST para o servidor usando o objeto fetch.
    const response = await fetch("http://localhost:8080/user", {
        method: "POST", // Método HTTP usado para a solicitação (POST neste caso).
        headers: new Headers({
            "Content-Type": "application/json; charset=utf8", // Tipo de conteúdo da requisição.
            Accept: "application/json", // Tipo de conteúdo esperado na resposta.
        }),
        body: JSON.stringify({
            username: username,
            password: password,
        }), // Corpo da requisição contendo dados no formato JSON.
    });
// Verifica se a resposta da requisição foi bem-sucedida (status 2xx).
    if (response.ok) {
        // Exibe um toast de sucesso usando a função showToast com o ID "#okToast".
        showToast("#okToast");
    } else {
        // Exibe um toast de erro usando a função showToast com o ID "#errorToast".
        showToast("#errorToast");
    }
}
// Função que exibe um toast com base no ID fornecido.
function showToast(id) {
    // Seleciona todos os elementos com o ID fornecido.
    var toastElList = [].slice.call(document.querySelectorAll(id));
    // Cria objetos Toast para cada elemento encontrado.
    var toastList = toastElList.map(function (toastEl) {
        return new bootstrap.Toast(toastEl);
    });
    // Exibe cada toast na lista.
    toastList.forEach((toast) => toast.show());
}