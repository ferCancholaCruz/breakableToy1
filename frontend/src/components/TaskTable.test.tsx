import { render, screen, fireEvent } from "@testing-library/react";
import React from "react";
import TaskTable from "./TaskTable";

beforeAll(() => {
  window.alert = jest.fn(); 
});

describe("TaskTable Component", () => {
  const mockTask = {
    id: 1,
    name: "Sample Task",
    priority: "High",
    dueDate: "2025-04-15",
    creationDate: "2025-04-01",
    done: false,
    doneDate: null,
  };

  const mockSetEditandoId = jest.fn();
  const mockFlagDone = jest.fn();
  const mockDeleteAct = jest.fn();
  const mockEditarTarea = jest.fn();
  const mockToggleSort = jest.fn();

  beforeEach(() => {
    render(
      <TaskTable
        tasks={[mockTask]}
        editandoId={null}
        setEditandoId={mockSetEditandoId}
        flagDone={mockFlagDone}
        deleteAct={mockDeleteAct}
        editarTarea={mockEditarTarea}
        toggleSort={mockToggleSort}
        priorityArrow="none"
        dueArrow="none"
        averageHigh={0}
        averageMedium={0}
        averageLow={0}
        averageAll={0}
      />
    );
  });

  test("renders table headers", () => {
    expect(screen.getByText(/^Done$/)).toBeInTheDocument();
    expect(screen.getByText(/Name/i)).toBeInTheDocument();
    expect(screen.getByText(/Priority/i)).toBeInTheDocument();
    expect(screen.getByText(/Due Date/i)).toBeInTheDocument();
    expect(screen.getByText(/Done Date/i)).toBeInTheDocument();
    expect(screen.getByText(/Actions/i)).toBeInTheDocument();
  });

  test("renders provided tasks", () => {
    expect(screen.getByText("Sample Task")).toBeInTheDocument();
    expect(screen.getByText("High")).toBeInTheDocument();
    expect(screen.getByText("2025-04-15")).toBeInTheDocument();
  });

  test("Edit and Delete buttons trigger callbacks", () => {
    const editButton = screen.getByText("Edit");
    const deleteButton = screen.getByText("Delete");

    fireEvent.click(editButton);
    fireEvent.click(deleteButton);

    expect(mockSetEditandoId).toHaveBeenCalledWith(1);
    expect(mockDeleteAct).toHaveBeenCalledWith(1);
  });

  test("row style change depending on due date limit", () => {
    const row = screen.getByText("Sample Task").closest("tr");
    expect(row).toHaveStyle("background-color: #f8d7da");
  });
});
