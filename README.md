# BreakToy - Backend

This is the backend API for BreakToy, a task management system with support for priorities, deadlines, and completion status.

## Table of Contents

- [Project Structure](#project-structure)
- [Technologies Used](#technologies-used)
- [Requirements](#requirements)
- [Sample JSON for Task Creation](#sample-json-for-task-creation)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Author](#author)

## Project Structure

src/main/java/com/breakabletoy1/breakToy/
├── controllers/
│   ├── HolaController.java
│   └── ToDoController.java
├── domain/
│   └── ToDo.java
├── helpers/
│   └── ToDoControllerHelper.java
├── repositoryLayer/
│   ├── InMemoryToDoRepository.java
│   └── ToDoRepository.java
├── services/
│   └── servicesToDo.java
├── sorts/
│   ├── SortBoth.java
│   ├── SortDueDate.java
│   └── SortPrior.java
└── BreakToyApplication.java

## Technologies Used

- Java 21
- Spring Boot 3.4.4
- Maven
- JUnit 5 and Mockito


## Requirements

- Java 21
- Maven 3.8+


## Sample JSON for Task Creation

json
{
  "name": "Study Java",
  "priority": "High",
  "dueDate": "2025-04-20"
}

## Running the Application

1. Open a terminal and navigate to the "breakToy" folder
2. Execute the command: mvn spring-boot:run

The backend will be running at port 9090


## API Endpoints

Method	   Endpoint	            Description
GET	       /todos	            Get all tasks (with filters)
POST	   /todos	            Create a new task
PUT	       /todos/{id}	        Update a task
DELETE	   /todos/{id}	        Delete a task
POST	   /todos/{id}/done	    Mark task as done
POST	   /todos/{id}/undone	Mark task as not done


## Running tests

The next command will start executing all the tests:
mvn test

This tests include Unit tests for the service layer using JUnit and Mockito 
and Controller tests using MockMvc or TestRestTemplate

## Author 
Canchola Cruz Fernando
