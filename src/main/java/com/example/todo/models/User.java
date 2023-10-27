package com.example.todo.models;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Size;

import com.example.todo.models.enums.ProfileEnum;
import com.fasterxml.jackson.annotation.JsonProperty;


import lombok.*;


@Entity // Esta classe representa uma entidade JPA (Java Persistence API).
@Table(name = User.TABLE_NAME) // Define o nome da tabela na DB.
@AllArgsConstructor // Anotação Lombok para gerar automaticamente um construtor com todos os campos.
@NoArgsConstructor // Anotação Lombok para gerar automaticamente um construtor vazio.
@Data // Anotação Lombok que gera automaticamente métodos getter, setter, equals, hashCode, etc.
public class User {



    public static final String TABLE_NAME = "user"; // Nome da tabela na base de dados

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Anotação JPA para gerar IDs automaticamente.
    @Column(name = "id", unique = true) // Define o nome da coluna na DB.
    private Long id;


    @Column(name = "username", length = 100, nullable = false, unique = true) // Define as propriedades da coluna "username".
    @NotBlank // Anotação de validação que requer que o campo não seja vazio.
    @Size(min = 2, max = 100) // Define o tamanho mínimo e máximo para o campo.
    private String username;


    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) //JsonProperty define que a coluna de password é só de escrita
    @Column(name = "password", length = 60, nullable = false) // Define as propriedades da coluna "password"
    @NotBlank
    @Size ( min = 8, max = 60)
    private String password;


    // one user may have many tasks
    @OneToMany (mappedBy = "user") // Define uma relação de um para muitos com a entidade "Task".
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // Configura a propriedade "tasks" para somente escrita.
    private List<Task> tasks = new ArrayList<Task>();

    @ElementCollection (fetch = FetchType.EAGER) // Define uma coleção de elementos.
    @JsonProperty (access = JsonProperty.Access.WRITE_ONLY) // Configura a propriedade "profiles" para somente escrita.
    @CollectionTable(name = "user_profile") // Define o nome da tabela para a coleção de perfis.
    @Column (name = "profile", nullable = false) // Define as propriedades da coluna "profile".
    private Set<Integer> profiles = new HashSet<>(); // Hashset because every profile is unique

    public Set<ProfileEnum> getProfiles() { // Set prohibits the use of duplicated elements
        return this.profiles.stream().map(x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet()); //Stream is used to process collections of objects.
    }

    public void addProfile(ProfileEnum profileEnum) {

        this.profiles.add(profileEnum.getCode());
    }

}
