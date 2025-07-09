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

        ToDo t1 = new ToDo(nextId++, "Submit project proposal", true, "High",
                LocalDate.of(2025, 6, 20), LocalDate.of(2025, 6, 22)); // 2 días
        t1.setDueDate(LocalDate.of(2025, 7, 10));
        tasks.add(t1);

        ToDo t2 = new ToDo(nextId++, "Grocery shopping", false, "Medium",
                LocalDate.of(2025, 6, 28), null);
        t2.setDueDate(LocalDate.of(2025, 7, 11));
        tasks.add(t2);

        ToDo t3 = new ToDo(nextId++, "Dentist appointment", true, "Low",
                LocalDate.of(2025, 6, 25), LocalDate.of(2025, 6, 26)); // 1 día
        t3.setDueDate(LocalDate.of(2025, 7, 12));
        tasks.add(t3);

        ToDo t4 = new ToDo(nextId++, "Write blog post", false, "High",
                LocalDate.of(2025, 6, 26), null);
        t4.setDueDate(LocalDate.of(2025, 7, 14));
        tasks.add(t4);

        ToDo t5 = new ToDo(nextId++, "Plan vacation", true, "Medium",
                LocalDate.of(2025, 6, 23), LocalDate.of(2025, 6, 27)); // 4 días
        t5.setDueDate(LocalDate.of(2025, 7, 13));
        tasks.add(t5);

        ToDo t6 = new ToDo(nextId++, "Read a book", false, "Low",
                LocalDate.of(2025, 6, 27), null);
        t6.setDueDate(LocalDate.of(2025, 7, 15));
        tasks.add(t6);

        ToDo t7 = new ToDo(nextId++, "Fix the bike", false, "Medium",
                LocalDate.of(2025, 6, 25), null);
        t7.setDueDate(LocalDate.of(2025, 7, 20));
        tasks.add(t7);

        ToDo t8 = new ToDo(nextId++, "Organize meeting", true, "High",
                LocalDate.of(2025, 6, 24), LocalDate.of(2025, 6, 25)); // 1 día
        t8.setDueDate(LocalDate.of(2025, 7, 16));
        tasks.add(t8);

        ToDo t9 = new ToDo(nextId++, "Code refactoring", false, "Medium",
                LocalDate.of(2025, 6, 29), null);
        t9.setDueDate(LocalDate.of(2025, 7, 17));
        tasks.add(t9);

        ToDo t10 = new ToDo(nextId++, "Water the plants", true, "Low",
                LocalDate.of(2025, 6, 25), LocalDate.of(2025, 6, 26)); // 1 día
        t10.setDueDate(LocalDate.of(2025, 7, 18));
        tasks.add(t10);

        ToDo t11 = new ToDo(nextId++, "Tidy up workspace", false, "High",
                LocalDate.of(2025, 6, 28), null);
        t11.setDueDate(LocalDate.of(2025, 7, 19));
        tasks.add(t11);

        ToDo t12 = new ToDo(nextId++, "Email client follow-up", true, "Medium",
                LocalDate.of(2025, 6, 24), LocalDate.of(2025, 6, 26)); // 2 días
        t12.setDueDate(LocalDate.of(2025, 7, 21));
        tasks.add(t12);

        ToDo t13 = new ToDo(nextId++, "Review pull requests", false, "High",
                LocalDate.of(2025, 6, 25), null);
        t13.setDueDate(LocalDate.of(2025, 7, 22));
        tasks.add(t13);

        ToDo t14 = new ToDo(nextId++, "Backup files", false, "Low",
                LocalDate.of(2025, 6, 24), null);
        t14.setDueDate(LocalDate.of(2025, 7, 23));
        tasks.add(t14);

        ToDo t15 = new ToDo(nextId++, "Practice presentation", true, "Medium",
                LocalDate.of(2025, 6, 24), LocalDate.of(2025, 6, 25)); // 1 día
        t15.setDueDate(LocalDate.of(2025, 7, 24));
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
