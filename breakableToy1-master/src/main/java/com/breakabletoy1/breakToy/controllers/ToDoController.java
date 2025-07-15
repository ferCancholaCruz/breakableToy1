package com.breakabletoy1.breakToy.controllers;

import com.breakabletoy1.breakToy.domain.ToDo;
import com.breakabletoy1.breakToy.services.ServicesToDo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class ToDoController {

    private final ServicesToDo service;

    public ToDoController(ServicesToDo service) {
        this.service = service;
    }

    @GetMapping
    public List<ToDo> getTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String order,
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String priority
    ) {
        return service.getFilteredTasks(page, order, done, name, priority);
    }

    @PostMapping
    public ToDo createTask(@RequestBody ToDo todo) {
        return service.create(todo);
    }

    @DeleteMapping("/{id}")
    public ToDo deleteTask(@PathVariable Long id) { // ID changed from int to Long to match entity
        return service.deleteById(id); // Uses Long now
    }

    @PutMapping("/{id}")
    public ToDo editTask(@RequestBody ToDo input, @PathVariable Long id) { //  ID changed
        return service.update(id, input); //  Uses Long now
    }

    @PostMapping("/{id}/done")
    public ToDo markDone(@PathVariable Long id) { //  ID changed
        return service.markDone(id); //  Uses Long now
    }

    @PostMapping("/{id}/undone")
    public ToDo markUndone(@PathVariable Long id) { //  ID changed
        return service.markUndone(id); //  Uses Long now
    }
}
