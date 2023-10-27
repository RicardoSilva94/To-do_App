package com.example.todo.repositories;


import com.example.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository // anotação que indica que esta interface é um repositório Spring.
public interface UserRepository extends JpaRepository <User, Long> {

    @Transactional(readOnly = true) // Esta anotação indica que o método findByUsername será executado em um contexto transacional somente leitura. Isso significa que o método não realizará operações de modificação na DB, apenas consultas.
    User findByUsername(String username);

}


//UserRepository é uma interface que extende JpaRepository. JpaRepository é uma interface fornecida pelo Spring Data JPA que oferece métodos predefinidos para realizar operações de persistência de dados em relação à entidade User.
// Fornece métodos para executar as operações de CRUD em objetos User na DB.