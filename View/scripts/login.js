async function login() {
    // Obtém os valores de username e password a partir dos elementos de input no file HTML.
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    console.log(username, password);

    // Envia uma requisição POST para o servidor para autenticar o user
    const response = await fetch("http://localhost:8080/login", {
        method: "POST",
        headers: new Headers({
            "Content-Type": "application/json; charset=utf8",
            Accept: "application/json",
        }),
        body: JSON.stringify({
            username: username,
            password: password,
        }),
    });

    // Obtém o token de autorização da resposta e armazena-o no armazenamento local (localStorage).
    let key = "Authorization";
    let token = response.headers.get(key);
    window.localStorage.setItem(key, token);

    // Verifica se a resposta da requisição foi bem-sucedida (status 2xx).
    if (response.ok) {
        // Exibe um toast de sucesso usando a função showToast com o ID "#okToast".
        showToast("#okToast");
    } else {
        // Exibe um toast de erro usando a função showToast com o ID "#errorToast".
        showToast("#errorToast");
    }
    // Redireciona o user para a página "/view/index.html" após 2 segundos.
    window.setTimeout(function () {
        window.location = "/view/index.html";
    }, 2000);
}

function showToast(id) {
    var toastElList = [].slice.call(document.querySelectorAll(id));
    var toastList = toastElList.map(function (toastEl) {
        return new bootstrap.Toast(toastEl);
    });
    toastList.forEach((toast) => toast.show());
}