export const fetchTasks = async (page: number, order?: string, filters: string = "") => {
    const partOrder = order ? `order=${order}` : "";
    const partPage = `page=${page}`;
    const query = `?${[partOrder, filters, partPage].filter(p => p).join("&")}`;
    const res = await fetch(`/todos${query}`);
    return res.json();
  };
  
  export const markTaskDone = (id: number) => fetch(`/todos/${id}/done`, { method: "POST" });
  export const markTaskUndone = (id: number) => fetch(`/todos/${id}/undone`, { method: "POST" });
  export const deleteTask = (id: number) => fetch(`/todos/${id}`, { method: "DELETE" });
  
  export const createTask = (data: any) =>
    fetch("/todos", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ...data, done: false }),
    });
  
  export const updateTask = (id: number, data: any) =>
    fetch(`/todos/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data),
    });