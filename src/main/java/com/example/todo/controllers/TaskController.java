package com.example.todo.controllers;

import com.example.todo.models.Task;
import com.example.todo.models.projection.TaskProjection;
import com.example.todo.services.TaskService;
import com.example.todo.services.UserService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/task")
@Validated
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    // Método para procurae uma tarefa por ID
    @GetMapping("/{id}")
    public ResponseEntity<Task> findById (@PathVariable Long id){
        // Chama o serviço para procurar a tarefa pelo ID
        Task obj = this.taskService.findById(id);
        // Retorna a tarefa encontrada como resposta com o status 200 OK
        return ResponseEntity.ok(obj);
    }

    // Método para obter todas as tarefas de um user
    @GetMapping("/user")
    public ResponseEntity<List<TaskProjection>> findAllByUser(){
        // Chama o serviço para procurar todas as tarefas do user autenticado
        List<TaskProjection> objs = this.taskService.findALLByUser();
        // Retorna a lista de tarefas como resposta com o status 200 OK
        return ResponseEntity.ok().body(objs);

    }

    // Método para criar uma nova tarefa
    @PostMapping
    @Validated
    public ResponseEntity<Void> create (@Valid @RequestBody Task obj){
        // Chama o serviço para criar a tarefa
        this.taskService.create(obj);
        // Cria a URI para a nova tarefa criada
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(obj.getId()).toUri();
        // Retorna uma resposta com o status 201 Created e a URI da nova tarefa
        return ResponseEntity.created(uri).build();
    }

    // Método para atualizar uma task existente
    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update (@Valid @RequestBody Task obj, @PathVariable Long id){
        // Define o ID da task com base no parâmetro da URL
        obj.setId(id);
        // Chama o serviço para atualizar a task
        this.taskService.update(obj);
        // Retorna uma resposta com o status 204 No Content
        return ResponseEntity.noContent().build();

    }

    // Método para eliminar uma tarefa por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        // Chama o serviço para eliminar a tarefa
        this.taskService.delete(id);
        // Retorna uma resposta com o status 204 No Content
        return ResponseEntity.noContent().build();

    }


}
