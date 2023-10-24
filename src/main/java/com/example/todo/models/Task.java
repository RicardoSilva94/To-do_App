package com.example.todo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;


@Entity
@Table(name = Task.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Task {
    public static final String TABLE_NAME = "task";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;


    @ManyToOne  //Many tasks belong to one user
    //
    @JoinColumn(name = "user_id", nullable = false, updatable = false) // join the user with the tasks
    private User user; //a user has many tasks

    @Column (name = "description", length = 255, nullable = false)
    @NotNull
    @NotEmpty
    @Size(min =1, max = 255)
    private String description;


}


