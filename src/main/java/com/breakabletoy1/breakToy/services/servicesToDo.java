package com.breakabletoy1.breakToy.services;

import com.breakabletoy1.breakToy.repositoryLayer.ToDoRepository;
import com.breakabletoy1.breakToy.domain.ToDo;
import com.breakabletoy1.breakToy.helpers.ToDoControllerHelper;
import com.breakabletoy1.breakToy.sorts.SortBoth;
import com.breakabletoy1.breakToy.sorts.SortDueDate;
import com.breakabletoy1.breakToy.sorts.SortPrior;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class servicesToDo {

    private static final int PAGE_SIZE = 10;
    private final ToDoRepository repository;

    public servicesToDo(ToDoRepository repository) {
        this.repository = repository;
    }

    public List<ToDo> getFilteredTasks(int page, String order, Boolean done, String name, String priority) {
        List<ToDo> filtered = new ArrayList<>(repository.findAll());

        if (done != null) {
            filtered = filtered.stream()
                    .filter(task -> task.getFlagDone() == done)
                    .collect(Collectors.toList());
        }

        if (name != null) {
            filtered = filtered.stream()
                    .filter(task -> task.getName().contains(name))
                    .collect(Collectors.toList());
        }

        if (priority != null) {
            filtered = filtered.stream()
                    .filter(task -> priority.equalsIgnoreCase(task.getPriority()))
                    .collect(Collectors.toList());
        }

        filtered = applySorting(filtered, order);
        return paginate(page, filtered);
    }

    private List<ToDo> applySorting(List<ToDo> tasks, String order) {
        if (order == null) return tasks;

        switch (order) {
            case "PriorityAsc" -> tasks.sort(new SortPrior());
            case "PriorityDesc" -> tasks.sort(Collections.reverseOrder(new SortPrior()));
            case "DueDateAsc" -> tasks.sort(new SortDueDate());
            case "DueDateDesc" -> tasks.sort(Collections.reverseOrder(new SortDueDate()));
            case "BothAsc" -> tasks.sort(new SortBoth());
            case "BothDesc" -> tasks.sort(Collections.reverseOrder(new SortBoth()));
            case "PriorityAscDueDesc" -> tasks.sort((a, b) -> {
                int p1 = ToDoControllerHelper.getPriorityValue(a.getPriority());
                int p2 = ToDoControllerHelper.getPriorityValue(b.getPriority());
                int comp = Integer.compare(p1, p2);

                if (comp == 0 && a.getDueDate() != null && b.getDueDate() != null) {
                    return b.getDueDate().compareTo(a.getDueDate());
                }
                return comp;
            });
            case "PriorityDescDueAsc" -> tasks.sort((a, b) -> {
                int p1 = ToDoControllerHelper.getPriorityValue(a.getPriority());
                int p2 = ToDoControllerHelper.getPriorityValue(b.getPriority());
                int comp = Integer.compare(p2, p1);

                if (comp == 0 && a.getDueDate() != null && b.getDueDate() != null) {
                    return a.getDueDate().compareTo(b.getDueDate());
                }
                return comp;
            });
        }

        return tasks;
    }

    private List<ToDo> paginate(int page, List<ToDo> list) {
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, list.size());
        if (start >= list.size()) return List.of();
        return list.subList(start, end);
    }

    public ToDo create(ToDo todo) {
        todo.setID(repository.getNextId());
        todo.setCreationDate(LocalDate.now());
        return repository.save(todo);
    }

    public ToDo deleteById(int id) {
        ToDo task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + id));
        repository.delete(id);
        return task;
    }

    public ToDo update(int id, ToDo input) {
        ToDo task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID " + id));

        if (input.getName() == null || input.getName().length() > 120) {
            throw new IllegalArgumentException("Name cannot be null or longer than 120 characters");
        }

        task.setName(input.getName());
        task.setDueDate(input.getDueDate());
        task.setPriority(input.getPriority());

        return repository.save(task);
    }

    public ToDo markDone(int id) {
        ToDo task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID " + id));

        task.setFlagDone(true);
        task.setDoneDate(LocalDate.now());

        return repository.save(task);
    }

    public ToDo markUndone(int id) {
        ToDo task = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID " + id));

        task.setFlagDone(false);
        task.setDoneDate(null);

        return repository.save(task);
    }
}
