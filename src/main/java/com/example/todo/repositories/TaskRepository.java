package com.example.todo.repositories;

import com.example.todo.models.Task;
import com.example.todo.models.projection.TaskProjection;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {



    //dentro do user, seleciona o id
    List<TaskProjection> findByUser_Id(Long id);

    //    @Query(value = "SELECT t FROM Task t WHERE t.user.id = :id")
  //  List<Task> findByUser_Id(@Param ("id") Long id);

//    @Query(value = "SELECT * FROM task t WHERE t.user_id = :id", nativeQuery = true)
  //  List<Task> findByUser_Id(@Param ("id") Long id);

}
//Repository são DAO's (Data access object)