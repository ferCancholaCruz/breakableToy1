package com.breakabletoy1.breakToy.services;

import com.breakabletoy1.breakToy.domain.ToDo;
import com.breakabletoy1.breakToy.repositoryLayer.ToDoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class servicesToDoTest {

    private ToDoRepository repository;
    private ServicesToDo service;

    @BeforeEach
    void setup() {
        repository = mock(ToDoRepository.class);
        service = new ServicesToDo(repository);
    }

    @Test
    void testCreateToDo() {
        ToDo input = new ToDo(null, "Test Task", false, "Medium", null, null);

        when(repository.save(any())).thenAnswer(invocation -> {
            ToDo saved = invocation.getArgument(0);
            saved.setID(1L);
            return saved;
        });

        ToDo result = service.create(input);

        assertEquals("Test Task", result.getName());
        assertEquals(1L, result.getID());
        assertEquals("Medium", result.getPriority());
        assertNotNull(result.getCreationDate());
    }

    @Test
    void testDeleteExistingToDo() {
        ToDo todo = new ToDo(5L, "Sample", false, "High", LocalDate.now(), null);
        when(repository.findById(5L)).thenReturn(Optional.of(todo));

        ToDo deleted = service.deleteById(5L);

        assertEquals(todo, deleted);
        verify(repository).deleteById(5L);
    }

    @Test
    void testUpdateToDoNameTooLong() {
        ToDo original = new ToDo(1L, "Old", false, "Low", LocalDate.now(), null);
        when(repository.findById(1L)).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class, () -> {
            ToDo updated = new ToDo();
            updated.setName("X".repeat(130));  // Esto lanza la excepción aquí, ahora sí dentro del assertThrows
            updated.setPriority("Low");
            updated.setDueDate(LocalDate.now());
            service.update(1L, updated);       // Esta línea ya no es necesaria si el setter lanza
        });
    }


    @Test
    void testMarkDoneSetsDateAndFlag() {
        ToDo task = new ToDo(2L, "Do this", false, "High", LocalDate.now(), null);
        when(repository.findById(2L)).thenReturn(Optional.of(task));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ToDo result = service.markDone(2L);

        assertTrue(result.getFlagDone());
        assertNotNull(result.getDoneDate());
    }

    @Test
    void testMarkUndoneClearsDateAndFlag() {
        ToDo task = new ToDo(3L, "Undo this", true, "Low", LocalDate.now(), LocalDate.now());
        when(repository.findById(3L)).thenReturn(Optional.of(task));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ToDo result = service.markUndone(3L);

        assertFalse(result.getFlagDone());
        assertNull(result.getDoneDate());
    }

    @Test
    void testUpdateToDoValidInput() {
        ToDo original = new ToDo(6L, "Initial", false, "Low", LocalDate.now(), null);
        when(repository.findById(6L)).thenReturn(Optional.of(original));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ToDo input = new ToDo();
        input.setName("Updated");
        input.setPriority("High");
        input.setDueDate(LocalDate.now().plusDays(2));

        ToDo result = service.update(6L, input);

        assertEquals("Updated", result.getName());
        assertEquals("High", result.getPriority());
    }

    @Test
    void testGetFilteredTasksReturnsPaginatedAndSorted() {
        List<ToDo> data = new ArrayList<>();
        for (long i = 1; i <= 15; i++) {
            data.add(new ToDo(i, "Task " + i, i % 2 == 0, "Medium", LocalDate.now(), null));
        }

        when(repository.findAll()).thenReturn(data);

        List<ToDo> result = service.getFilteredTasks(0, null, null, null, null);

        assertEquals(10, result.size()); // PAGE_SIZE is 10
        assertEquals("Task 1", result.get(0).getName());
    }
}
