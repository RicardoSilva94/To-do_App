package com.example.todo.controllers;

import com.example.todo.models.User;
import com.example.todo.models.dto.UserCreateDTO;
import com.example.todo.models.dto.UserUpdateDTO;
import com.example.todo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

// Anotação que marca a classe UserController como um controlador Spring que lida com solicitações HTTP.
// Combina as anotações @Controller e @ResponseBody, indicando que os métodos nesta classe retornarão respostas HTTP com dados no corpo da resposta, como JSON.
@RestController
//Esta anotação mapeia a classe UserController para lidar com todas as solicitações HTTP que tenham o caminho base "/user".
//Qualquer solicitação que comece com "/user" será manipulada por métodos nesta classe.
@RequestMapping("/user")
//Esta anotação pode ser usada para ativar a validação de métodos em um controlador.
//Permite a validação das entradas dos métodos usando anotações de validação, como @Valid, nas assinaturas dos métodos.
@Validated
public class UserController {

    @Autowired
    private UserService userService;



    // Método para obter um utilizador por ID
//A anotação PathVariable é usada para mapear a variável de caminho "id" na URL para o parâmetro Long id do método.
//Permite ao método findById aceder ao valor do "id" presente na URL.
    //O tipo de retorno do método findById é ResponseEntity<User>, que permite ao controlador retornar uma resposta HTTP personalizada.
    @GetMapping("/{id}")
    public ResponseEntity<User> findById (@PathVariable Long id) {
        // Chama o serviço para procurar o user pelo ID
        User obj = this.userService.findById(id);
        // Retorna o user encontrado como resposta com o status 200 OK
        return ResponseEntity.ok().body(obj);

    }

    // Método para criar um novo user
    // @RequestBddy indica que o parâmetro do método (UserCreateDTO obj) é vinculado ao corpo da solicitação HTTP.
    //É usado para deserializar automaticamente o JSON da solicitação HTTP num objeto UserCreateDTO.
    //@Valid é usada para ativar a validação do objeto UserCreateDTO.
    //Quando usada em conjunto com @RequestBody, ela valida o objeto DTO com base nas anotações de validação presentes na classe UserCreateDTO.

    @PostMapping()
    public ResponseEntity<Void> create(@Valid @RequestBody UserCreateDTO obj){
        // Converte o DTO de criação de user num objeto de user
        User user = this.userService.fromDTO(obj);
        // Chama o serviço para criar o user
        User newUser = this.userService.create(user);
        // Cria a URI para o novo user criado
        //ServletUri.... é usado neste método para construir a URI do novo recurso criado. O fromCurrentRequest() pega a URI atual da requisição, e path("/{id}") adiciona o ID do novo user à URI.
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId()).toUri();
        // Retorna uma resposta com o status 201 Created e a URI do novo user
        return ResponseEntity.created(uri).build();
    }

    // Método para atualizar um user existente
    @PutMapping("/{id}")

    public ResponseEntity<Void> update(@Valid @RequestBody UserUpdateDTO obj, @PathVariable Long id){
        // Converte o DTO de atualização de user em um objeto de user
        User user = this.userService.fromDTO(obj);
        // Define o ID com base no parâmetro da URL
        obj.setId(id);
        // Chama o serviço para atualizar o user
        this.userService.update(user);
        // Retorna uma resposta com o status 204 No Content
        return ResponseEntity.noContent().build();
    }

    // Método para excluir um user por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        // Chama o serviço para excluir o user
        this.userService.delete(id);
        // Retorna uma resposta com o status 204 No Content
        return ResponseEntity.noContent().build();

    }

}

// As classes de controlador (Controller) são responsáveis por lidar com as requisições HTTP, apresentar informações ao utilizador e fornecer uma interface para interagir com a aplicação
