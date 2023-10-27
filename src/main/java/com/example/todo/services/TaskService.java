package com.example.todo.services;

import com.example.todo.models.enums.ProfileEnum;
import com.example.todo.models.projection.TaskProjection;
import com.example.todo.repositories.TaskRepository;
import com.example.todo.security.UserSpringSecurity;
import com.example.todo.services.exceptions.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.todo.models.Task;
import com.example.todo.models.User;

import com.example.todo.services.exceptions.DataBindingViolationException;
import com.example.todo.services.exceptions.ObjectNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    // Método para encontrar uma tarefa por ID
    public Task findById(Long id){
        // Tenta encontrar a tarefa pelo ID; se não encontrar, lança uma exceção
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
                "Task not found! Id: " + id + ", Type: " + Task.class.getName()
        ));
        // Obtém as informações do user autenticado
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        // Verifica se o user autenticado tem permissão para aceder á tarefa
        if (Objects.isNull(userSpringSecurity)
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Access denied!");

        // Retorna a tarefa se tudo estiver correto
        return task;
    }

    // Método para encontrar todas as tarefas de um user autenticado
    public List<TaskProjection> findALLByUser(){
        // Obtém as informações do user autenticado
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        // Verifica se o user autenticado está disponível
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Access denied!");

        // Obtém as tarefas associadas ao ID do user autenticado
        List<TaskProjection> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());

        return tasks;
    }

    // Método para criar uma nova tarefa
    @Transactional
    public Task create(Task obj) {
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Access denied!");
        // Obtém as informações do user autenticado a partir do serviço do userService
        User user = this.userService.findById(userSpringSecurity.getId());
        // Define o ID da tarefa como nulo e associa a tarefa ao user
        obj.setId(null);
        obj.setUser(user);
        // Guarda a tarefa no repositório
        obj = this.taskRepository.save(obj);
        // Devolve a tarefa criada
        return obj;
    }

    // Método para atualizar uma tarefa
    @Transactional
    public Task update(Task obj) {
        // Obtém a tarefa existente pelo ID
        Task newObj = findById(obj.getId());
        // Atualiza a descrição da tarefa
        newObj.setDescription(obj.getDescription());
        // Guarda a tarefa atualizada no repositório
        return this.taskRepository.save(newObj);
    }

    // Método para apagar uma tarefa por ID
    public void delete(Long id) {
        // Verifica se a tarefa existe antes de tentar apagá-la
        findById(id);
        try {
            // Tenta eliminar a tarefa pelo ID
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            // Lança uma exceção se houver entidades relacionadas com a tarefa
            throw new DataBindingViolationException("Error deleting because there are related entities");
        }
    }

    // Método privado para verificar se um user tem uma determinada tarefa
    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task) {
        return task.getUser().getId().equals(userSpringSecurity.getId());
    }

}
