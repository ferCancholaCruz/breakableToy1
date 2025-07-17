package com.breakabletoy1.breakToy.services;

import com.breakabletoy1.breakToy.repositoryLayer.ToDoRepository;
import com.breakabletoy1.breakToy.domain.ToDo;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class ServicesToDo {

    private static final int PAGE_SIZE = 10;
    private final ToDoRepository repository;

    public ServicesToDo(ToDoRepository repository) {
        this.repository = repository;
    }

    public Page<ToDo> getFilteredTasks(int page, String order, Boolean done, String name, String priority) {
        Sort sort = getSort(order);
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, sort);

        // Lógica dinámica: se delega completamente al repositorio
        if (done != null && name != null && priority != null) {
            return repository.findByFlagDoneAndNameContainingIgnoreCaseAndPriorityIgnoreCase(done, name, priority, pageable);
        } else if (done != null && name != null) {
            return repository.findByFlagDoneAndNameContainingIgnoreCase(done, name, pageable);
        } else if (done != null && priority != null) {
            return repository.findByFlagDoneAndPriorityIgnoreCase(done, priority, pageable);
        } else if (name != null && priority != null) {
            return repository.findByNameContainingIgnoreCaseAndPriorityIgnoreCase(name, priority, pageable);
        } else if (done != null) {
            return repository.findByFlagDone(done, pageable);
        } else if (name != null) {
            return repository.findByNameContainingIgnoreCase(name, pageable);
        } else if (priority != null) {
            return repository.findByPriorityIgnoreCase(priority, pageable);
        } else {
            return repository.findAll(pageable);
        }
    }

    private Sort getSort(String order) {
        if (order == null || order.isEmpty()) {
            return Sort.by("id").ascending(); // por defecto
        }

        return switch (order) {
            case "PriorityAsc" -> Sort.by("priority").ascending();
            case "PriorityDesc" -> Sort.by("priority").descending();
            case "DueDateAsc" -> Sort.by("dueDate").ascending();
            case "DueDateDesc" -> Sort.by("dueDate").descending();
            case "BothAsc" -> Sort.by("priority").ascending().and(Sort.by("dueDate").ascending());
            case "BothDesc" -> Sort.by("priority").descending().and(Sort.by("dueDate").descending());
            case "PriorityAscDueDesc" -> Sort.by("priority").ascending().and(Sort.by("dueDate").descending());
            case "PriorityDescDueAsc" -> Sort.by("priority").descending().and(Sort.by("dueDate").ascending());
            default -> Sort.by("id").ascending();
        };
    }

    public ToDo create(ToDo todo) {
        todo.setCreationDate(LocalDate.now());
        return repository.save(todo);
    }

    public ToDo deleteById(Long id) {
        ToDo task = getOrThrow(id);
        repository.deleteById(id);
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
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Task not found with ID: " + id));
    }
}
