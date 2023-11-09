package com.example.todo.services;

import com.example.todo.models.User;
import com.example.todo.repositories.UserRepository;
import com.example.todo.security.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // Implementação da interface UserDetailsService para carregar informações do user
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Procura um utilizador pelo nome de user fornecido
        User user = this.userRepository.findByUsername(username);
        // Se nenhum user for encontrado, lança uma exceção UsernameNotFoundException
        if (Objects.isNull(user))
            throw new UsernameNotFoundException("User not found: " + username);
        // Cria um objeto UserSpringSecurity para representar o user no contexto do Spring Security
        return new UserSpringSecurity(user.getId(), user.getUsername(), user.getPassword(),user.getProfiles());
    }
}
