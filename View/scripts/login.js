async function login() {
    // Obtém os valores de username e password a partir dos elementos de input no file HTML.
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    console.log(username, password);

    try {
        const response = await fetch("http://localhost:8080/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json; charset=utf-8",
                Accept: "application/json",
            },
            body: JSON.stringify({
                username: username,
                password: password,
            }),
        });

        if (!response.ok) {
            throw new Error("Login failed");
        }

        // Verifica se o cabeçalho contém o token de autorização antes de tentar acessá-lo
        let key = "Authorization";
        if (response.headers.has(key)) {
            let token = response.headers.get(key);
            window.localStorage.setItem(key, token);
        }

        showToast("#okToast");

        // Redireciona o usuário para a página "/view/index.html" após 2 segundos.
        window.setTimeout(function () {
            window.location = "/To-do/view/index.html";
        }, 2000);
    } catch (error) {
        showToast("#errorToast");
        console.error("Error:", error);
    }
}

function showToast(id) {
    var toastElList = [].slice.call(document.querySelectorAll(id));
    var toastList = toastElList.map(function (toastEl) {
        return new bootstrap.Toast(toastEl);
    });
    toastList.forEach((toast) => toast.show());
}