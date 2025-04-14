# BreakToy API Documentation

This document describes the RESTful API for the BreakToy backend, a task management system that handles creation, filtering, editing, and tracking of task completion.

## Table of Contents

- [Base URL](#base-url)
- [Endpoints](#endpoints)
    - [GET /todos](#get-todos)
    - [POST /todos](#post-todos)
    - [PUT /todos{id}](#put-todosid)
    - [DELETE /todos{id}](#delete-todosid)
    - [POST /todos{id}/done](#post-todosiddone)
    - [POST /todos{id}/undone](#post-todosidundone)
- [Sample curl Requests](#sample-curl-requests)
- [Notes](#notes)
- [Author](#author)


## Base URL
http://localhost:9090

## Endpoints

### GET /todos

Retrieve all tasks, with optional filters and sorting.


| Parameter | Type    | Description                                           |
|-----------|---------|-------------------------------------------------------|
| page      | number  | Page number for pagination (default is 0)            |
| name      | string  | Filter tasks containing this name                    |
| priority  | string  | Filter by priority: `High`, `Medium`, `Low`          |
| done      | boolean | Filter by completion status: `true` or `false`       |
| order     | string  | Sort order (`PriorityAsc`, `DueDateDesc`, etc.)      |


json
[
  {
    "id": 1,
    "name": "Sample task guide",
    "priority": "High",
    "creationDate": "2025-04-01",
    "dueDate": "2025-04-20",
    "flagDone": false,
    "doneDate": null
  }
]

## POST /todos
Create a new task.

Request Body:

{
"name": "New Task",
"priority": "Medium",
"dueDate": "2025-05-01"
}

Returns the created task with generated id and creationDate.

Response: 200 OK


## PUT /todos/{id}
Update a task's details.

Path Parameter:

id: Task ID to update

Request Body:
{
"name": "Updated Task",
"priority": "Low",
"dueDate": "2025-05-10"
}

Response: 200 OK with the updated task

## DELETE /todos/{id}
Delete a task by its ID.

Response: 200 OK with the deleted task data

## POST /todos/{id}/done
Mark a task as completed. This also sets the doneDate.

Response: 200 OK with updated task

## POST /todos/{id}/undone
Unmark a completed task. This clears the doneDate.

Response: 200 OK with updated task


## Sample curl Requests
1. Get all tasks:
curl -X GET http://localhost:9090/todos

2. Create a task:
curl -X POST http://localhost:9090/todos \
-H "Content-Type: application/json" \
-d '{"name": "New task", "priority": "High", "dueDate": "2025-04-25"}'

3. Mark a task as done:
curl -X POST http://localhost:9090/todos/1/done

4. Delete a task:
curl -X DELETE http://localhost:9090/todos/1


## Notes
Tasks must have a name (max 120 characters) and a priority.

doneDate is automatically managed by the server when marking tasks done or undone.

Pagination is set to 10 items per page.

## Author
Canchola Cruz Fernando