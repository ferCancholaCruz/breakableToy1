import React, { useEffect, useState } from "react";
import TodoForm from "../components/TodoForm";
import TaskTable from "../components/TaskTable";
import TaskFilters from "../components/TaskFilters";
import {
  fetchTasks,
  markTaskDone,
  markTaskUndone,
  deleteTask,
  createTask,
  updateTask,
} from "../services/TaskService";


interface Task {
  id: number;
  name: string;
  dueDate: string;
  priority: string;
  done: boolean;
  doneDate: string | null;
  creationDate: string;
}


interface TaskFormData {
  name: string;
  dueDate: string;
  priority: string;
}

const TodoList = () => {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [priorityArrow, setPriorityArrow] = useState<"none" | "asc" | "desc">("none");
  const [dueArrow, setDueArrow] = useState<"none" | "asc" | "desc">("none");
  const [nameFilter, setNameFilter] = useState("");
  const [priorityFilter, setPriorityFilter] = useState("");
  const [statusFilter, setStatusFilter] = useState("");
  const [currentPage, setCurrentPage] = useState(0);
  const [averageHigh, setAverageHigh] = useState(0);
  const [averageMedium, setAverageMedium] = useState(0);
  const [averageLow, setAverageLow] = useState(0);
  const [averageAll, setAverageAll] = useState(0);
  const [showForm, setShowForm] = useState(false);
  const [currentOrder, setCurrentOrder] = useState<string | undefined>();
  const [filterString, setFilterString] = useState<string>("");

  useEffect(() => {
    loadTasks(currentOrder, filterString);
    fetchAllPaginatedTasks();
  }, [currentPage, currentOrder, filterString]);

  const loadTasks = async (order?: string, filters: string = "") => {
    setCurrentOrder(order);
    setFilterString(filters);
    const rawData = await fetchTasks(currentPage, order, filters) || [];
    const transformed = rawData.map((task: any) => ({
      ...task,
      done: task.flagDone,
    }));
    setTasks(transformed);
  };

  const fetchAllPaginatedTasks = async () => {
    let page = 0;
    let allTasks: Task[] = [];
    let keepGoing = true;

    while (keepGoing) {
      const rawData = await fetchTasks(page, currentOrder, filterString) || [];
      const transformed = rawData.map((task: any) => ({
        ...task,
        done: task.flagDone,
      }));
      allTasks = [...allTasks, ...transformed];
      keepGoing = transformed.length === 10;
      page++;
    }

    calculateAverages(allTasks);
  };

  const calculateAverages = (list: Task[]) => {
    const high: number[] = [], medium: number[] = [], low: number[] = [];
    list.forEach((t) => {
      if (t.done && t.creationDate && t.doneDate) {
        const diff = new Date(t.doneDate).getTime() - new Date(t.creationDate).getTime();
        const minutes = Math.ceil(diff / (1000 * 60));
        if (t.priority === "High") high.push(minutes);
        else if (t.priority === "Medium") medium.push(minutes);
        else if (t.priority === "Low") low.push(minutes);
      }
    });
    const total = (arr: number[]) => arr.reduce((sum, val) => sum + val, 0);
    const avg = (arr: number[]) => (arr.length ? total(arr) / arr.length : 0);
    setAverageHigh(avg(high));
    setAverageMedium(avg(medium));
    setAverageLow(avg(low));
    setAverageAll(avg([...high, ...medium, ...low]));
  };

  const applyFilter = () => {
    let filters: string[] = [];
    if (nameFilter) filters.push(`name=${nameFilter}`);
    if (priorityFilter) filters.push(`priority=${priorityFilter}`);
    if (statusFilter === "Done") filters.push("done=true");
    if (statusFilter === "Undone") filters.push("done=false");
    const filterString = filters.length > 0 ? filters.join("&") : "";
    loadTasks(undefined, filterString);
  };

  const toggleSort = (column: "Priority" | "DueDate") => {
    let newPriority = priorityArrow;
    let newDue = dueArrow;
    if (column === "Priority") {
      newPriority = priorityArrow === "asc" ? "desc" : priorityArrow === "desc" ? "none" : "asc";
      setPriorityArrow(newPriority);
    } else {
      newDue = dueArrow === "asc" ? "desc" : dueArrow === "desc" ? "none" : "asc";
      setDueArrow(newDue);
    }

    let order: string | undefined;
    if (newPriority === "asc" && newDue === "desc") order = "PriorityAscDueDesc";
    else if (newPriority === "desc" && newDue === "asc") order = "PriorityDescDueAsc";
    else if (newPriority === "asc" && newDue === "asc") order = "BothAsc";
    else if (newPriority === "desc" && newDue === "desc") order = "BothDesc";
    else if (newPriority === "asc") order = "PriorityAsc";
    else if (newPriority === "desc") order = "PriorityDesc";
    else if (newDue === "asc") order = "DueDateAsc";
    else if (newDue === "desc") order = "DueDateDesc";

    loadTasks(order);
  };

  const flagDone = async (id: number, done: boolean, shouldReload = true) => {
    await (done ? markTaskUndone(id) : markTaskDone(id));
    if (shouldReload) {
      await loadTasks(currentOrder, filterString);
      await fetchAllPaginatedTasks();
    }
  };

  const checkAll = async () => {
    await Promise.all(tasks.map(t => flagDone(t.id, true, false)));
    await loadTasks(currentOrder, filterString);
  };

  const uncheckAll = async () => {
    await Promise.all(tasks.map(t => flagDone(t.id, false, false)));
    await loadTasks(currentOrder, filterString);
  };

  const deleteTaskById = async (id: number) => {
    await deleteTask(id);
    loadTasks();
  };

  const createNewTask = async (data: TaskFormData) => {
    await createTask(data);
    setShowForm(false);
    loadTasks();
  };

  const editTask = async (id: number, data: TaskFormData) => {
    await updateTask(id, data);
    setEditingId(null);
    loadTasks();
  };

  return (
    <div>
      <h2 className="task-title">Task Manager</h2>

      <div className="filter-box">
        <TaskFilters
          nameFilter={nameFilter} setNameFilter={setNameFilter}
          priorFilter={priorityFilter} setPriorFilter={setPriorityFilter}
          stateFilter={statusFilter} setStateFilter={setStatusFilter}
          onApplyFilter={applyFilter}
        />
      </div>

      {!showForm && (
        <button className="add-task-button" onClick={() => setShowForm(true)}>
          + Add New Task
        </button>
      )}
      {showForm && <TodoForm modo="crear" onSubmit={createNewTask} onCancel={() => setShowForm(false)} />}

      <TaskTable
        tasks={tasks}
        editandoId={editingId}
        setEditandoId={setEditingId}
        flagDone={flagDone}
        deleteAct={deleteTaskById}
        editarTarea={editTask}
        toggleSort={toggleSort}
        priorityArrow={priorityArrow}
        dueArrow={dueArrow}
        averageHigh={averageHigh}
        averageMedium={averageMedium}
        averageLow={averageLow}
        averageAll={averageAll}
        onCheckAll={checkAll}
        onUncheckAll={uncheckAll}
      />

      <div className="pagination-container">
        <button onClick={() => setCurrentPage(p => Math.max(p - 5, 0))} disabled={currentPage < 5}>
          ⏪ Back 5
        </button>
        <button onClick={() => setCurrentPage(p => Math.max(p - 1, 0))} disabled={currentPage === 0}>
          Previous
        </button>
        <span style={{ margin: "0 10px" }}>Page {currentPage + 1}</span>
        <button onClick={() => setCurrentPage(p => p + 1)} disabled={tasks.length < 10}>
          Next
        </button>
        <button onClick={() => setCurrentPage(p => p + 5)} disabled={tasks.length < 10}>
          ⏩ Forward 5
        </button>
      </div>

      <div className="averages-container">
        <div className="average-block">
          <p><strong>Overall average:</strong> {averageAll.toFixed(2)} minutes</p>
        </div>
        <div className="average-block">
          <p>High Priority: {averageHigh.toFixed(2)} minutes</p>
          <p>Medium Priority: {averageMedium.toFixed(2)} minutes</p>
          <p>Low Priority: {averageLow.toFixed(2)} minutes</p>
        </div>
      </div>
    </div>
  );
};

export default TodoList;
