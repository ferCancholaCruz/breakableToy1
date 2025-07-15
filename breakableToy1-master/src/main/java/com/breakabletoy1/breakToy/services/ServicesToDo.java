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
public class ServicesToDo {

    private static final int PAGE_SIZE = 10;
    private final ToDoRepository repository;

    public ServicesToDo(ToDoRepository repository) {
        this.repository = repository;
    }

    public List<ToDo> getFilteredTasks(int page, String order, Boolean done, String name, String priority) {
        List<ToDo> filtered = applyFilters(done, name, priority);
        filtered = applySorting(filtered, order);
        return paginate(page, filtered);
    }

    private List<ToDo> applyFilters(Boolean done, String name, String priority) {
        return repository.findAll().stream()
                .filter(task -> done == null || task.getFlagDone() == done)
                .filter(task -> name == null || task.getName().contains(name))
                .filter(task -> priority == null || priority.equalsIgnoreCase(task.getPriority()))
                .collect(Collectors.toList());
    }

    private List<ToDo> applySorting(List<ToDo> tasks, String order) {
        tasks.sort(getSort(order));
        return tasks;
    }

    private Comparator<ToDo> getSort(String order) {
        if (order == null || order.isEmpty()) {
            return Comparator.comparingLong(ToDo::getID); // ✅ Changed from comparingInt to comparingLong
        }

        return switch (order) {
            case "PriorityAsc" -> new SortPrior();
            case "PriorityDesc" -> Collections.reverseOrder(new SortPrior());
            case "DueDateAsc" -> new SortDueDate();
            case "DueDateDesc" -> Collections.reverseOrder(new SortDueDate());
            case "BothAsc" -> new SortBoth();
            case "BothDesc" -> Collections.reverseOrder(new SortBoth());
            case "PriorityAscDueDesc" -> priorityThenDueDate(true, false);
            case "PriorityDescDueAsc" -> priorityThenDueDate(false, true);
            default -> Comparator.comparingLong(ToDo::getID); // ✅ Changed from comparingInt to comparingLong
        };
    }

    private Comparator<ToDo> priorityThenDueDate(boolean ascPriority, boolean ascDueDate) {
        return (a, b) -> {
            int p1 = ToDoControllerHelper.getPriorityValue(a.getPriority());
            int p2 = ToDoControllerHelper.getPriorityValue(b.getPriority());
            int comp = ascPriority ? Integer.compare(p1, p2) : Integer.compare(p2, p1);

            if (comp == 0 && a.getDueDate() != null && b.getDueDate() != null) {
                return ascDueDate
                        ? a.getDueDate().compareTo(b.getDueDate())
                        : b.getDueDate().compareTo(a.getDueDate());
            }
            return comp;
        };
    }

    private List<ToDo> paginate(int page, List<ToDo> list) {
        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, list.size());
        if (start >= list.size()) return List.of();
        return list.subList(start, end);
    }

    public ToDo create(ToDo todo) {
        // Removed manual ID assignment — now handled by JPA via @GeneratedValue
        todo.setCreationDate(LocalDate.now());
        return repository.save(todo);
    }

    public ToDo deleteById(Long id) {
        ToDo task = getOrThrow(id);
        repository.deleteById(id); //  Using Spring Data JPA method
        return task;
    }

    public ToDo update(Long id, ToDo input) {
        ToDo task = getOrThrow(id);
        validateUpdateInput(input);

        task.setName(input.getName());
        task.setDueDate(input.getDueDate());
        task.setPriority(input.getPriority());

        return repository.save(task);
    }

    private void validateUpdateInput(ToDo input) {
        if (input.getName() == null || input.getName().length() > 120) {
            throw new IllegalArgumentException("Name cannot be null or longer than 120 characters");
        }
    }

    public ToDo markDone(Long id) {
        ToDo task = getOrThrow(id);
        task.setFlagDone(true);
        task.setDoneDate(LocalDate.now());
        return repository.save(task);
    }

    public ToDo markUndone(Long id) {
        ToDo task = getOrThrow(id);
        task.setFlagDone(false);
        task.setDoneDate(null);
        return repository.save(task);
    }

    private ToDo getOrThrow(Long id) {
        // ✅ Switched to Long instead of int, and using JPA method
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + id));
    }
}
