package com.example.todo.controllers;

import com.example.todo.models.Task;
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

    @GetMapping("/{id}")
    public ResponseEntity<Task> findById (@PathVariable Long id){
        Task obj = this.taskService.findById(id);
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/user")
    public ResponseEntity<List<Task>> findAllByUser(){
    List<Task> objs = this.taskService.findALLByUser();
    return ResponseEntity.ok().body(objs);

    }

    @PostMapping
    @Validated
    public ResponseEntity<Void> create (@Valid @RequestBody Task obj){
        this.taskService.create(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<Void> update (@Valid @RequestBody Task obj, @PathVariable Long id){
        obj.setId(id);
        this.taskService.update(obj);
        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete (@PathVariable Long id){
        this.taskService.delete(id);
        return ResponseEntity.noContent().build();

    }


}
