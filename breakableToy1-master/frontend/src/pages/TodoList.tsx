import React from "react";
import TodoForm from "../components/TodoForm";
import TaskTable from "../components/TaskTable";
import TaskFilters from "../components/TaskFilters";
import "../styles/TodoListStyles.css";

import { deleteTask, createTask, updateTask } from "../services/TaskService";
import { TaskFormData } from "../Types";
import { useTaskList } from "../hooks/useTaskList";

const TodoList = () => {
  const {
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
  } = useTaskList();

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

  const deleteAndReload = async (id: number) => {
    await deleteTask(id);
    loadTasks();
  };

  const toggleDarkMode = () => {
    document.body.classList.toggle("dark-mode");
  };

  return (
    <div>
      {/* 🌙 Toggle Dark Mode Button */}
      <div style={{ textAlign: "right", margin: "10px 30px" }}>
        <button className="toggle-dark-mode" onClick={toggleDarkMode}>
          🌙 Toggle Dark Mode
        </button>
      </div>

      <h2 className="task-title">Task Manager</h2>

      <div className="filter-box">
        <TaskFilters
          nameFilter={filters.name}
          setNameFilter={filters.setName}
          priorFilter={filters.priority}
          setPriorFilter={filters.setPriority}
          stateFilter={filters.status}
          setStateFilter={filters.setStatus}
          onApplyFilter={() => loadTasks()}
        />
      </div>

      {!showForm && (
        <button className="add-task-button" onClick={() => setShowForm(true)}>
          + Add New Task
        </button>
      )}

      {showForm && (
        <TodoForm
          modo="crear"
          onSubmit={createNewTask}
          onCancel={() => setShowForm(false)}
        />
      )}

      <TaskTable
        tasks={tasks}
        editandoId={editingId}
        setEditandoId={setEditingId}
        flagDone={flagDone}
        deleteAct={deleteAndReload}
        editarTarea={editTask}
        toggleSort={toggleSort}
        priorityArrow={priorityArrow}
        dueArrow={dueArrow}
        averageHigh={stats.values.High}
        averageMedium={stats.values.Medium}
        averageLow={stats.values.Low}
        averageAll={stats.values.All}
        onCheckAll={checkAll}
        onUncheckAll={uncheckAll}
      />

      <div className="pagination-container">
        <button
          onClick={() => setCurrentPage(p => Math.max(p - 5, 0))}
          disabled={currentPage < 5}
        >
          ⏪ Back 5
        </button>
        <button
          onClick={() => setCurrentPage(p => Math.max(p - 1, 0))}
          disabled={currentPage === 0}
        >
          Previous
        </button>
        <span style={{ margin: "0 10px" }}>Page {currentPage + 1}</span>
        <button
          onClick={() => setCurrentPage(p => p + 1)}
          disabled={tasks.length < 10}
        >
          Next
        </button>
        <button
          onClick={() => setCurrentPage(p => p + 5)}
          disabled={tasks.length < 10}
        >
          ⏩ Forward 5
        </button>
      </div>

      <div className="averages-container">
        <div className="average-block">
          <p>
            <strong>Overall average:</strong> {stats.formatted.averageAll}
          </p>
        </div>
        <div className="average-block">
          <p>
            <span className="priority-indicator priority-high"></span>
            High Priority: {stats.formatted.averageHigh}
          </p>
          <p>
            <span className="priority-indicator priority-medium"></span>
            Medium Priority: {stats.formatted.averageMedium}
          </p>
          <p>
            <span className="priority-indicator priority-low"></span>
            Low Priority: {stats.formatted.averageLow}
          </p>
        </div>
      </div>
    </div>
  );
};

export default TodoList;
