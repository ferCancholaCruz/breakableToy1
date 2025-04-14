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
    private servicesToDo service;

    @BeforeEach
    void setup() {
        repository = mock(ToDoRepository.class);
        service = new servicesToDo(repository);
    }

    @Test
    void testCreateToDo() {
        ToDo input = new ToDo(0, "Test Task", false, "Medium", null, null);

        when(repository.getNextId()).thenReturn(1);
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ToDo result = service.create(input);

        assertEquals("Test Task", result.getName());
        assertEquals(1, result.getID());
        assertEquals("Medium", result.getPriority());
        assertNotNull(result.getCreationDate());
    }

    @Test
    void testDeleteExistingToDo() {
        ToDo todo = new ToDo(5, "Sample", false, "High", LocalDate.now(), null);
        when(repository.findById(5)).thenReturn(Optional.of(todo));

        ToDo deleted = service.deleteById(5);

        assertEquals(todo, deleted);
        verify(repository).delete(5);
    }

    @Test
    void testUpdateToDoNameTooLong() {
        ToDo original = new ToDo(1, "Old", false, "Low", LocalDate.now(), null);
        ToDo updated = new ToDo(1, "Valid", false, "Low", LocalDate.now(), null);

        // Evitamos validación al usar setter
        updated.setName("X".repeat(130));

        when(repository.findById(1)).thenReturn(Optional.of(original));

        assertThrows(IllegalArgumentException.class, () -> service.update(1, updated));
    }


    @Test
    void testMarkDoneSetsDateAndFlag() {
        ToDo task = new ToDo(2, "Do this", false, "High", LocalDate.now(), null);
        when(repository.findById(2)).thenReturn(Optional.of(task));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ToDo result = service.markDone(2);

        assertTrue(result.getFlagDone());
        assertNotNull(result.getDoneDate());
    }

    @Test
    void testMarkUndoneClearsDateAndFlag() {
        ToDo task = new ToDo(3, "Undo this", true, "Low", LocalDate.now(), LocalDate.now());
        when(repository.findById(3)).thenReturn(Optional.of(task));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ToDo result = service.markUndone(3);

        assertFalse(result.getFlagDone());
        assertNull(result.getDoneDate());
    }


}
