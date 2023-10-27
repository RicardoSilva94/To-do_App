package com.example.todo.services;


import com.example.todo.models.User;
import com.example.todo.models.dto.UserCreateDTO;
import com.example.todo.models.dto.UserUpdateDTO;
import com.example.todo.models.enums.ProfileEnum;
import com.example.todo.repositories.UserRepository;
import com.example.todo.security.UserSpringSecurity;
import com.example.todo.services.exceptions.AuthorizationException;
import com.example.todo.services.exceptions.DataBindingViolationException;
import com.example.todo.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired //for dependency injection
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


    // Método para encontrar um user por ID
     public User findById(Long id){
         UserSpringSecurity userSpringSecurity = authenticated(); // Obtém as informações do user autenticado

         // Verifica se o user autenticado tem permissão para aceder ao recurso
         if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId()))
             throw new AuthorizationException("Access denied!");

         // Procura o user pelo ID e lança uma exceção se não for encontrado
        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException("User not found! Id: " + id + ", Type: " + User.class.getName()
        ));
    }


    // Método para criar um novo user
    @Transactional // Se algo der errado durante a execução do método, as alterações feitas na DB serão revertidas (rollback).
    public User create(User obj){
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword())); // criptografa a pass do user antes de a colocar na DB
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet())); // define perfil do utilizador como "USER"
        obj = this.userRepository.save(obj); //  Aqui, o objeto User é salvo na BD usando o repositório UserRepository
        return obj;
    }



    // Método para atualizar informações de um user
    @Transactional
    public User update(User obj){
        User newObj = findById(obj.getId());
        newObj.setPassword(obj.getPassword());
        newObj.setPassword(this.bCryptPasswordEncoder.encode((obj.getPassword())));
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        return this.userRepository.save(newObj);

    }

    // Método para eliminar um user por ID
    public void delete(Long id){
        // Verifica se o user existe antes de tentar eliminá-lo
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e){
            throw new DataBindingViolationException("It's not possible to delete because there's related entities");
        }
    }

    // Método para obter o user autenticado
    public static UserSpringSecurity authenticated(){
        try{
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            return null;
        }
    }

    // Método para converter um DTO (UserCreateDTO) em um objeto User
    public User fromDTO(@Valid UserCreateDTO obj) {  // a anotação @Valid é usada para aplicar validações ao objeto DTO, garantindo que os dados estejam num formato aceitável antes da conversão.
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());
        return user;
    }

    // Método para converter um DTO (UserUpdateDTO) em um objeto User
    public User fromDTO(@Valid UserUpdateDTO obj) {
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());
        return user;
    }


}

//classes de serviço são responsáveis pela lógica de negócios e operações de dados