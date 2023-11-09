package com.example.todo.models.projection;

public interface TaskProjection {

    public Long getId();

    public String getDescription();

}

//Esta interface é frequentemente usada em conjunto com consultas JPA ou consultas personalizadas em bases de dados, onde queremos especificar explicitamente quais os campos de uma entidade devem ser recuperados numa consulta, em vez de trazer todos os campos.
//No contexto de Spring Data JPA, projeções como esta podem ser usadas para otimizar a recuperação de dados de entidades, economizando recursos e melhorando o desempenho, especialmente quando não se precisa de todos os campos da entidade numa determinada consulta.