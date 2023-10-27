package com.example.todo.exceptions;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.sound.midi.Sequence;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
public class ErrorResponse {
    private final int status; // O status HTTP da resposta de erro
    private final String message; // A mensagem de erro
    private String stackTrace; // O rastreamento da stack de erros
    private List<ValidationError> errors; // Lista de erros de validação

    // Método para serializar o objeto ErrorResponse em JSON
    public String toJson() {
        return "{\"status\": " + getStatus() + ", " +
                "\"message\": \"" + getMessage() + "\"}";
    }

    @Getter
    @Setter
    @RequiredArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL) // Anotação usada na classe interna ValidationError para indicar que campos nulos não devem ser incluídos na representação JSON quando a classe é serializada.
    // Portanto, se um campo field for nulo em um objeto ValidationError, ele não será incluído na representação JSON final.
    private static class ValidationError{
        private final String field; // O campo que falhou na validação
        private final String message; // A mensagem de erro relacionada ao campo
    }

    // Método para adicionar um erro de validação à lista de erros
    public void addValidationError(String field, String message){
        if(Objects.isNull(errors)){
            this.errors = new ArrayList<>();
        }
        this.errors.add(new ValidationError(field, message));
    }

}
