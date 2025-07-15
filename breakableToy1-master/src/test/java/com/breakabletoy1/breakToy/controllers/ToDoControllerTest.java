package com.breakabletoy1.breakToy.controllers;

import com.breakabletoy1.breakToy.domain.ToDo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ToDoControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    void testGetTodosShouldReturnList() {
        ResponseEntity<ToDo[]> response = restTemplate.getForEntity(url("/todos"), ToDo[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ToDo[] todos = response.getBody();
        assertNotNull(todos);
    }

    @Test
    void testCreateTodo() {
        ToDo newTodo = new ToDo(null, "Integration test", false, "High", LocalDate.now(), null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ToDo> request = new HttpEntity<>(newTodo, headers);

        ResponseEntity<ToDo> response = restTemplate.postForEntity(url("/todos"), request, ToDo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ToDo created = response.getBody();
        assertNotNull(created);
        assertEquals("Integration test", created.getName());
    }

    @Test
    void testUpdateTodo() {
        // Primero creamos uno
        ToDo original = new ToDo(null, "To update", false, "Low", LocalDate.now(), null);
        HttpEntity<ToDo> createRequest = new HttpEntity<>(original, new HttpHeaders() {{
            setContentType(MediaType.APPLICATION_JSON);
        }});
        ToDo created = restTemplate.postForEntity(url("/todos"), createRequest, ToDo.class).getBody();
        assertNotNull(created);

        // Ahora actualizamos el nombre
        created.setName("Updated task");
        HttpEntity<ToDo> updateRequest = new HttpEntity<>(created, new HttpHeaders() {{
            setContentType(MediaType.APPLICATION_JSON);
        }});
        ResponseEntity<ToDo> response = restTemplate.exchange(url("/todos/" + created.getID()), HttpMethod.PUT, updateRequest, ToDo.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated task", response.getBody().getName());
    }

    @Test
    void testMarkDone() {
        ToDo todo = new ToDo(null, "Mark me done", false, "Medium", LocalDate.now(), null);
        ToDo created = restTemplate.postForEntity(url("/todos"), new HttpEntity<>(todo, jsonHeaders()), ToDo.class).getBody();

        ResponseEntity<ToDo> response = restTemplate.postForEntity(url("/todos/" + created.getID() + "/done"), null, ToDo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().getFlagDone());
        assertNotNull(response.getBody().getDoneDate());
    }

    @Test
    void testMarkUndone() {
        ToDo todo = new ToDo(null, "Mark me undone", true, "High", LocalDate.now(), LocalDate.now());
        ToDo created = restTemplate.postForEntity(url("/todos"), new HttpEntity<>(todo, jsonHeaders()), ToDo.class).getBody();

        ResponseEntity<ToDo> response = restTemplate.postForEntity(url("/todos/" + created.getID() + "/undone"), null, ToDo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().getFlagDone());
        assertNull(response.getBody().getDoneDate());
    }

    @Test
    void testDeleteTodo() {
        ToDo todo = new ToDo(null, "To delete", false, "Low", LocalDate.now(), null);
        ToDo created = restTemplate.postForEntity(url("/todos"), new HttpEntity<>(todo, jsonHeaders()), ToDo.class).getBody();

        ResponseEntity<ToDo> response = restTemplate.exchange(url("/todos/" + created.getID()), HttpMethod.DELETE, null, ToDo.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("To delete", response.getBody().getName());
    }

    // Helper
    private HttpHeaders jsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}
