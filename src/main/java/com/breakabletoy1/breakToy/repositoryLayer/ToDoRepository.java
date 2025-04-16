package com.breakabletoy1.breakToy.repositoryLayer;

import com.breakabletoy1.breakToy.domain.ToDo;
import java.util.List;
import java.util.Optional;


public interface ToDoRepository {

    List<ToDo> findAll();

    Optional<ToDo> findById(int id);

    ToDo save(ToDo todo);

    void delete(int id);

    int getNextId();
}

