import React, { useEffect, useState } from "react";
import TodoForm from "./TodoForm";

interface Todo {
  id: number;
  name: string;
  dueDate: string;
  priority: string;
  flag: boolean;
  doneDate: string | null;
  creationDate: string;
}

interface DatosForm {
  name: string;
  dueDate: string;
  priority: string;
}

const TodoList = () => {
  const [tareas, setTareas] = useState<Todo[]>([]);
  const [editandoId, setEditandoId] = useState<number | null>(null);
  const [activeSortField, setActiveSortField] = useState<string | null>(null);
  const [isAscending, setIsAscending] = useState(true);
  const [mainSort, setMainSort] = useState<string | null>(null); 
  const [secondarySort, setSecondarySort] = useState<string | null>(null);
  const [priorityArrow, setPriorityArrow] = useState<"none" | "asc" | "desc">("none");
  const [dueArrow, setDueArrow] = useState<"none" | "asc" | "desc">("none");
  const [nameFilter, setNameFilter] = useState("");
  const [priorFilter, setPriorFilter] = useState("");
  const [stateFilter, setStateFilter] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [totalPages, setTotalPages] = useState(1);


  // Cargar tareas al inicio
  useEffect(() => {
    cargarTareas();
  }, [currentPage]);

  const cargarTareas = async (orden? : string, filtros : string="") => {
    const partOrder = orden ? `orden=${orden}` : "";
    const partPage = `ind1=${currentPage}`;
    const union = partOrder && filtros ? "&" : "";
    const finalUrl = `?${[partOrder, filtros, partPage].filter(p => p).join("&")}`;
    const res = await fetch(`/todos${finalUrl}`);
    const data = await res.json();
    setTareas(data);
  };


  const applyFilter = () => {
    let filters: string[] = [];
  
    if (nameFilter) filters.push(`name=${nameFilter}`);
    if (priorFilter) filters.push(`priority=${priorFilter}`);
    if (stateFilter === "Done") filters.push("done=true");
    if (stateFilter === "Undone") filters.push("done=false");
  
    const filterString = filters.length > 0 ? filters.join("&") : "";
  
    cargarTareas(undefined, filterString);
  };
  

const cambiarOrden = (clickedField: string ) => {
  if (activeSortField !== clickedField) {  //new field start with asc
    setActiveSortField(clickedField);
    setIsAscending(true);
    cargarTareas(`${clickedField}Asc`);
  } else if (isAscending) {   //same filed to desc
    setIsAscending(false);
    cargarTareas(`${clickedField}Desc`);
  } else {   //same field but no order
    setActiveSortField(null);
    setIsAscending(true); 
    cargarTareas(); // no order
  }
};

const toggleSort = (columna: "Priority" | "DueDate") => {
  let newPriority = priorityArrow;
  let newDue = dueArrow;

  if (columna === "Priority") {
    // Cambiar el estado de prioridad
    if (priorityArrow === "asc") {
      newPriority = "desc";
    } else if (priorityArrow === "desc") {
      newPriority = "none";
    } else {
      newPriority = "asc";
    }

    setPriorityArrow(newPriority);
  } else {
    // State of DueDate
    if (dueArrow === "asc") {
      newDue = "desc";
    } else if (dueArrow === "desc") {
      newDue = "none";
    } else {
      newDue = "asc";
    }

    setDueArrow(newDue);
  }

  // String for sort in the backend
  let orden: string | undefined;

  if (newPriority === "asc" && newDue === "desc") {
    orden = "PriorityAscDueDesc";
  } else if (newPriority === "desc" && newDue === "asc") {
    orden = "PriorityDescDueAsc";
  } else if (newPriority === "asc") {
    orden = "PriorityAsc";
  } else if (newPriority === "desc") {
    orden = "PriorityDesc";
  } else if (newDue === "asc") {
    orden = "DueDateAsc";
  } else if (newDue === "desc") {
    orden = "DueDateDesc";
  } else {
    orden = undefined; // No sort
  }

  // Send order to backend
  cargarTareas(orden);
};


const getArrow = (state: "none" | "asc" | "desc") => {
  if (state === "asc") return "🔼";
  if (state === "desc") return "🔽";
  return "⬍";
};

  const toggleDone = async (id: number, flag: boolean) => {
    const endpoint = flag ? `/todos/${id}/undone` : `/todos/${id}/done`;
    await fetch(endpoint, { method: "POST" });
    cargarTareas();
  };

  const deleteAct = async (id: number) => {
    await fetch(`/todos/${id}`, { method: "DELETE" });
    cargarTareas();
  };

  const crearTarea = async (datos: DatosForm) => {
    await fetch("/todos", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ...datos, flag: false }),
    });
    cargarTareas();
  };

  const editarTarea = async (id: number, datos: DatosForm) => {
    await fetch(`/todos/${id}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(datos),
    });
    setEditandoId(null);
    cargarTareas();
  };

  

  return (
    <div>
      <h2>Create new task</h2>
      <TodoForm modo="crear" onSubmit={crearTarea} />
  
      <h2>Filter tasks</h2>
      <div style={{ marginBottom: "20px" }}>
        <input
          type="text"
          placeholder="Search by name"
          value={nameFilter}
          onChange={(e) => setNameFilter(e.target.value)}
          style={{ marginRight: "10px" }}
        />
  
        <select
          value={priorFilter}
          onChange={(e) => setPriorFilter(e.target.value)}
          style={{ marginRight: "10px" }}
        >
          <option value="">All</option>
          <option value="High">High</option>
          <option value="Medium">Medium</option>
          <option value="Low">Low</option>
        </select>
  
        <select
          value={stateFilter}
          onChange={(e) => setStateFilter(e.target.value)}
          style={{ marginRight: "10px" }}
        >
          <option value="">All</option>
          <option value="Done">Done</option>
          <option value="Undone">Undone</option>
        </select>
  
        <button onClick={applyFilter}>Search</button>
      </div>
  
      <h2>To Do List</h2>
      <table>
        <thead>
          <tr>
            <th>Done</th>
            <th>Name</th>
            <th
              onClick={() => toggleSort("Priority")}
              style={{ cursor: "pointer" }}
            >
              Priority {getArrow(priorityArrow)}
            </th>
            <th
              onClick={() => toggleSort("DueDate")}
              style={{ cursor: "pointer" }}
            >
              Due Date {getArrow(dueArrow)}
            </th>
            <th>Done Date</th>
            <th>Actions</th>
          </tr>
        </thead>
  
        <tbody>
          {tareas.map((t) => (
            <tr key={t.id}>
              <td>
                <input
                  type="checkbox"
                  checked={t.flag}
                  onChange={() => toggleDone(t.id, t.flag)}
                />
              </td>
  
              {editandoId === t.id ? (
                <>
                  <td colSpan={4}>
                    <TodoForm
                      modo="editar"
                      valoresIniciales={{
                        name: t.name,
                        dueDate: t.dueDate,
                        priority: t.priority,
                      }}
                      onSubmit={(datos) => editarTarea(t.id, datos)}
                    />
                  </td>
                  <td>
                    <button onClick={() => setEditandoId(null)}>Cancel</button>
                  </td>
                </>
              ) : (
                <>
                  <td>{t.name}</td>
                  <td>{t.priority}</td>
                  <td>{t.dueDate}</td>
                  <td>{t.doneDate || "-"}</td>
                  <td>
                    <button onClick={() => setEditandoId(t.id)}>Edit</button>
                    <button onClick={() => deleteAct(t.id)}>Delete</button>
                  </td>
                </>
              )}
            </tr>
          ))}
        </tbody>
      </table>
  
      {/* Paginación: FUERA de la tabla */}
      <div style={{ marginTop: "20px" }}>
        <button
          onClick={() => setCurrentPage(currentPage - 1)}
          disabled={currentPage === 0}
        >
          Previous
        </button>
  
        <span style={{ margin: "0 10px" }}>Page {currentPage + 1}</span>
  
        <button
          onClick={() => setCurrentPage(currentPage + 1)}
          disabled={tareas.length < 10}
        >
          Next
        </button>
      </div>
    </div>
  );
              }  

export default TodoList;

