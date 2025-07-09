import { useEffect, useState } from "react";
import { fetchTasks, markTaskDone, markTaskUndone } from "../services/TaskService";
import { Task } from "../Types";
import { useTaskFilters } from "./useTaskFilters";
import { useTaskStats } from "./useTaskStats";

export function useTaskList() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [showForm, setShowForm] = useState(false);
  const [currentPage, setCurrentPage] = useState(0);
  const [priorityArrow, setPriorityArrow] = useState<"none" | "asc" | "desc">("none");
  const [dueArrow, setDueArrow] = useState<"none" | "asc" | "desc">("none");
  const [order, setOrder] = useState<string | undefined>();

  const filters = useTaskFilters();
  const stats = useTaskStats();

  useEffect(() => {
    loadTasks();
    fetchAllPaginatedTasks();
  }, [currentPage, order, filters.buildFilterString()]);

  const loadTasks = async () => {
    const raw = await fetchTasks(currentPage, order, filters.buildFilterString()) || [];
    const transformed = raw.map((t: any) => ({ ...t, done: t.flagDone }));
    setTasks(transformed);
  };

  const fetchAllPaginatedTasks = async () => {
    let page = 0;
    let all: Task[] = [];
    let keepGoing = true;
    while (keepGoing) {
      const raw = await fetchTasks(page, order, filters.buildFilterString()) || [];
      const transformed = raw.map((t: any) => ({ ...t, done: t.flagDone }));
      all = [...all, ...transformed];
      keepGoing = transformed.length === 10;
      page++;
    }
    stats.updateStats(all);
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

    let newOrder;
    if (newPriority === "asc") newOrder = "PriorityAsc";
    else if (newPriority === "desc") newOrder = "PriorityDesc";
    else if (newDue === "asc") newOrder = "DueDateAsc";
    else if (newDue === "desc") newOrder = "DueDateDesc";

    setOrder(newOrder);
  };

  const flagDone = async (id: number, done: boolean, reload = true) => {
    await (done ? markTaskUndone(id) : markTaskDone(id));
    if (reload) {
      await loadTasks();
      await fetchAllPaginatedTasks();
    }
  };

  const checkAll = async () => {
    for (const t of tasks) {
      if (!t.done) await flagDone(t.id, false, false);
    }
    await loadTasks();
    await fetchAllPaginatedTasks();
  };

  const uncheckAll = async () => {
    for (const t of tasks) {
      if (t.done) await flagDone(t.id, true, false);
    }
    await loadTasks();
    await fetchAllPaginatedTasks();
  };

  return {
    tasks,
    editingId, setEditingId,
    showForm, setShowForm,
    currentPage, setCurrentPage,
    priorityArrow, dueArrow,
    toggleSort,
    filters,
    stats,
    loadTasks,
    flagDone,
    checkAll,
    uncheckAll,
  };
}
