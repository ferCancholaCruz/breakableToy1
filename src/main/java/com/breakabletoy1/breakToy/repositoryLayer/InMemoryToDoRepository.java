package com.breakabletoy1.breakToy.repositoryLayer;

import com.breakabletoy1.breakToy.domain.ToDo;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;

import java.util.*;

@Repository
public class InMemoryToDoRepository implements ToDoRepository {

    private final List<ToDo> tasks = new ArrayList<>();
    private int nextId = 1;

    public InMemoryToDoRepository() {

        ToDo t1 = new ToDo(nextId++, "Submit project proposal", true, "High", LocalDate.of(2025, 4, 1), LocalDate.of(2025, 4, 5));
        t1.setDueDate(LocalDate.of(2025, 4, 22));
        tasks.add(t1);

        ToDo t2 = new ToDo(nextId++, "Grocery shopping", false, "Medium", LocalDate.of(2025, 4, 10), null);
        t2.setDueDate(LocalDate.of(2025, 5, 11));
        tasks.add(t2);

        ToDo t3 = new ToDo(nextId++, "Dentist appointment", true, "Low", LocalDate.of(2025, 3, 20), LocalDate.of(2025, 3, 22));
        t3.setDueDate(LocalDate.of(2025, 4, 27));
        tasks.add(t3);

        ToDo t4 = new ToDo(nextId++, "Write blog post", false, "High", LocalDate.of(2025, 4, 8), null);
        t4.setDueDate(LocalDate.of(2025, 4, 18));
        tasks.add(t4);

        ToDo t5 = new ToDo(nextId++, "Plan vacation", true, "Medium", LocalDate.of(2025, 3, 15), LocalDate.of(2025, 3, 25));
        t5.setDueDate(LocalDate.of(2025, 5, 1));
        tasks.add(t5);

        ToDo t6 = new ToDo(nextId++, "Read a book", false, "Low", LocalDate.of(2025, 4, 10), null);
        t6.setDueDate(LocalDate.of(2025, 4, 20));
        tasks.add(t6);

        ToDo t7 = new ToDo(nextId++, "Fix the bike", false, "Medium", LocalDate.of(2025, 4, 9), LocalDate.of(2025, 4, 12));
        t7.setDueDate(LocalDate.of(2025, 4, 25));
        tasks.add(t7);

        ToDo t8 = new ToDo(nextId++, "Organize meeting", true, "High", LocalDate.of(2025, 4, 6), LocalDate.of(2025, 4, 6));
        t8.setDueDate(LocalDate.of(2025, 4, 28));
        tasks.add(t8);

        ToDo t9 = new ToDo(nextId++, "Code refactoring", false, "Medium", LocalDate.of(2025, 4, 13), null);
        t9.setDueDate(LocalDate.of(2025, 5, 18));
        tasks.add(t9);

        ToDo t10 = new ToDo(nextId++, "Water the plants", true, "Low", LocalDate.of(2025, 3, 30), LocalDate.of(2025, 4, 1));
        t10.setDueDate(LocalDate.of(2025, 4, 30));
        tasks.add(t10);

        ToDo t11 = new ToDo(nextId++, "Tidy up workspace", false, "High", LocalDate.of(2025, 4, 11), null);
        t11.setDueDate(LocalDate.of(2025, 4, 19));
        tasks.add(t11);

        ToDo t12 = new ToDo(nextId++, "Email client follow-up", true, "Medium", LocalDate.of(2025, 4, 2), LocalDate.of(2025, 4, 3));
        t12.setDueDate(LocalDate.of(2025, 4, 27));
        tasks.add(t12);

        ToDo t13 = new ToDo(nextId++, "Review pull requests", false, "High", LocalDate.of(2025, 4, 4), null);
        t13.setDueDate(LocalDate.of(2025, 5, 5));
        tasks.add(t13);

        ToDo t14 = new ToDo(nextId++, "Backup files", false, "Low", LocalDate.of(2025, 4, 1), null);
        t14.setDueDate(LocalDate.of(2025, 4, 27));
        tasks.add(t14);

        ToDo t15 = new ToDo(nextId++, "Practice presentation", true, "Medium", LocalDate.of(2025, 4, 5), LocalDate.of(2025, 4, 6));
        t15.setDueDate(LocalDate.of(2025, 4, 26));
        tasks.add(t15);



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
