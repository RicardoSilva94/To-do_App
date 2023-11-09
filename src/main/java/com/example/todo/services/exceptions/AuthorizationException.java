package com.example.todo.services.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.springframework.security.access.AccessDeniedException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class AuthorizationException extends AccessDeniedException {

    public AuthorizationException (String message){
        super(message);
    }

}

// é uma classe que representa uma exceção personalizada relacionada com erros de autorização no sistema.