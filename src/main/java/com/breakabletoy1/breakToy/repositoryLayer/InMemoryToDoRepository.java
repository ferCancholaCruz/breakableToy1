package com.breakabletoy1.breakToy.repositoryLayer;

import com.breakabletoy1.breakToy.domain.ToDo;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryToDoRepository implements ToDoRepository {

    private final List<ToDo> tasks = new ArrayList<>();
    private int nextId = 1;

    public InMemoryToDoRepository() {

        tasks.add(new ToDo(
                nextId++,
                "Submit project proposal",
                true,
                "High",
                java.time.LocalDate.of(2025, 4, 1),
                java.time.LocalDate.of(2025, 4, 5)
        ));

        tasks.add(new ToDo(
                nextId++,
                "Grocery shopping",
                false,
                "Medium",
                java.time.LocalDate.of(2025, 4, 10),
                null
        ));

        tasks.add(new ToDo(
                nextId++,
                "Dentist appointment",
                true,
                "Low",
                java.time.LocalDate.of(2025, 3, 20),
                java.time.LocalDate.of(2025, 3, 22)
        ));

        tasks.add(new ToDo(
                nextId++,
                "Write blog post",
                false,
                "High",
                java.time.LocalDate.of(2025, 4, 8),
                null
        ));

        tasks.add(new ToDo(
                nextId++,
                "Plan vacation",
                true,
                "Medium",
                java.time.LocalDate.of(2025, 3, 15),
                java.time.LocalDate.of(2025, 3, 25)
        ));
    }

    @Override
    public List<ToDo> findAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public Optional<ToDo> findById(int id) {
        return tasks.stream()
                .filter(t -> t.getID() == id)
                .findFirst();
    }

    @Override
    public ToDo save(ToDo todo) {
        delete(todo.getID()); // para evitar duplicados
        tasks.add(todo);
        return todo;
    }

    @Override
    public void delete(int id) {
        tasks.removeIf(t -> t.getID() == id);
    }

    @Override
    public int getNextId() {
        return nextId++;
    }
}
