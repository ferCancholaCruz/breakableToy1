## BreakToy - Frontend

This is the frontend of BreakToy, a task management system built with React and TypeScript. It allows users to create, view, update, delete, and filter tasks based on priority, status, and due date. The UI also provides real-time averages for task completion times by priority level.

## Features

- Add, edit, and delete tasks
- Mark tasks as done or undone
- Filter tasks by name, priority, and status
- Sort tasks by priority and due date
- Paginate task results
- Display average completion time per priority level

## Technologies Used

- React (with TypeScript)
- CSS (`styles/TodoStyles.css`)
- Fetch API for HTTP requests
- Functional components and hooks (`useState`, `useEffect`)

## Folder Structure


## Requirements

- Node.js v18+
- npm or yarn

## Setup and Run

1. Navigate to the frontend directory

bash
cd frontend

2. Install dependencies
npm install

3.  Start the development server
npm run start

## API Communication

This frontend expects the backend REST API to be available at:

http://localhost:9090/todos

## Sample Task JSON (used in POST/PUT requests)
{
  "name": "Sample Task ",
  "dueDate": "2025-04-20",
  "priority": "Medium"
}

## Author
Canchola Cruz Fernando