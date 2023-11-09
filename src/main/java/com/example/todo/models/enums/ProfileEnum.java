package com.example.todo.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum ProfileEnum {
    // Definição dos valores enumerados com códigos e descrições
    ADMIN(1, "ROLE_ADMIN"),
    USER(2, "ROLE_USER");

    private Integer code; // Código numérico associado ao valor enumerado
    private String description; // Descrição do valor enumerado

    // Método para converter um código em um valor do enum
    public static ProfileEnum toEnum(Integer code) {
        if (Objects.isNull(code)) // Verifica se o código é nulo
            return null;

// Itera sobre os valores do enum e compara o código
        for (ProfileEnum x : ProfileEnum.values()) {

            if (code.equals(x.getCode()))
                return x; // Retorna o valor do enum se houver correspondência
        }
// Se nenhum valor corresponder ao código, lança uma exceção
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
