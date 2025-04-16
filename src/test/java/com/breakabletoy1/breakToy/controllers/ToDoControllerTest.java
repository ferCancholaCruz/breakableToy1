package com.breakabletoy1.breakToy.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.boot.test.web.client.TestRestTemplate;

import com.breakabletoy1.breakToy.domain.ToDo;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ToDoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetTodosShouldReturnList() {
        var response = restTemplate.getForEntity("http://localhost:" + port + "/todos", ToDo[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ToDo[] todos = response.getBody();
        assertNotNull(todos);
        assertTrue(todos.length >= 1); // porque cargás una al iniciar
    }
}
