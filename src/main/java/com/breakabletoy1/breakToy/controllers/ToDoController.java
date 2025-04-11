package com.breakabletoy1.breakToy.controllers;

import com.breakabletoy1.breakToy.domain.ToDo;
import com.breakabletoy1.breakToy.services.servicesToDo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class ToDoController {

    private final servicesToDo service;

    public ToDoController(servicesToDo service) {
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
    public ToDo deleteTask(@PathVariable int id) {
        return service.deleteById(id);
    }

    @PutMapping("/{id}")
    public ToDo editTask(@RequestBody ToDo input, @PathVariable int id) {
        return service.update(id, input);
    }

    @PostMapping("/{id}/done")
    public ToDo markDone(@PathVariable int id) {
        return service.markDone(id);
    }

    @PostMapping("/{id}/undone")
    public ToDo markUndone(@PathVariable int id) {
        return service.markUndone(id);
    }
}


