// Helper function to convert Date objects to "YYYY-MM-DD" string
function formatDate(date: any) {
  return date ? new Date(date).toISOString().split("T")[0] : null;
}

// Fetch list of tasks with pagination and filters
export const fetchTasks = async (page: number, order?: string, filters: string = "") => {
  const partOrder = order ? `order=${order}` : "";
  const partPage = `page=${page}`;
  const query = `?${[partOrder, filters, partPage].filter(p => p).join("&")}`;
  const res = await fetch(`/todos${query}`);
  return res.json();
};

// Mark a task as done (POST to /todos/{id}/done)
export const markTaskDone = (id: number) =>
  fetch(`/todos/${id}/done`, { method: "POST" });

// Mark a task as undone (POST to /todos/{id}/undone)
export const markTaskUndone = (id: number) =>
  fetch(`/todos/${id}/undone`, { method: "POST" });

// Delete a task by ID (DELETE to /todos/{id})
export const deleteTask = (id: number) =>
  fetch(`/todos/${id}`, { method: "DELETE" });

// Create a new task (POST to /todos)
// Dates are formatted correctly and flagDone is initialized
export const createTask = (data: any) =>
  fetch("/todos", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      ...data,
      flagDone: false, // Default state for new task
      dueDate: formatDate(data.dueDate),
      creationDate: formatDate(data.creationDate),
      doneDate: formatDate(data.doneDate),
    }),
  });

// Update an existing task (PUT to /todos/{id})
// Dates are formatted before sending
export const updateTask = (id: number, data: any) =>
  fetch(`/todos/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      ...data,
      dueDate: formatDate(data.dueDate),
      creationDate: formatDate(data.creationDate),
      doneDate: formatDate(data.doneDate),
    }),
  });
