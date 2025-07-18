import React, { useEffect, useState } from "react";
import TodoForm from "./TodoForm";
import "../styles/TaskTableStyles.css";
import { Task, TaskTableProps } from "../Types";

const getFont = (done: boolean): string => (done ? "doneTask" : "");

const getArrow = (state: "none" | "asc" | "desc") => {
  if (state === "asc") return "\u25B2";
  if (state === "desc") return "\u25BC";
  return "\u21C5";
};

const TaskTable: React.FC<TaskTableProps> = ({
  tasks,
  editandoId,
  setEditandoId,
  flagDone,
  deleteAct,
  editarTarea,
  toggleSort,
  priorityArrow,
  dueArrow,
  averageHigh,
  averageMedium,
  averageLow,
  averageAll,
  onCheckAll,
  onUncheckAll
}) => {
  const [checkAllActive, setCheckAllActive] = useState(false);

  useEffect(() => {
    const allChecked = tasks.length > 0 && tasks.every(t => t.done);
    setCheckAllActive(allChecked);
  }, [tasks]);

  const handleHeaderCheckboxChange = async () => {
    if (!checkAllActive) {
      await onCheckAll();
    } else {
      await onUncheckAll();
    }
  };

  return (
    <>
      <table className="task-table">
        <thead>
          <tr>
            <th>
              <input
                type="checkbox"
                checked={checkAllActive}
                onChange={handleHeaderCheckboxChange}
              />
              Done
            </th>
            <th>Name</th>
            <th onClick={() => toggleSort("Priority")} style={{ cursor: "pointer" }}>
              Priority {getArrow(priorityArrow)}
            </th>
            <th onClick={() => toggleSort("DueDate")} style={{ cursor: "pointer" }}>
              Due Date {getArrow(dueArrow)}
            </th>
            <th>Done Date</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {tasks.map((t) => (
            <tr
              key={t.id}
              className={`priority-${t.priority}`}
            >
              <td>
                <input
                  type="checkbox"
                  checked={!!t.done}
                  onChange={() => flagDone(t.id, t.done)}
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
                      onCancel={() => setEditandoId(null)}
                    />
                  </td>
                </>
              ) : (
                <>
                  <td className={getFont(t.done)}>{t.name}</td>
                  <td>{t.priority}</td>
                  <td>{t.dueDate}</td>
                  <td>{t.doneDate || "-"}</td>
                  <td>
                    <button className="edit-button" onClick={() => setEditandoId(t.id)}>Edit</button>
                    <button className="delete-button" onClick={() => deleteAct(t.id)}>Delete</button>
                  </td>
                </>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </>
  );
};

export default TaskTable;
