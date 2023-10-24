package com.example.todo.repositories;


import com.example.todo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface UserRepository extends JpaRepository <User, Long> {

    @Transactional(readOnly = true)
    User findByUsername(String username);

}
