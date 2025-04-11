import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import { act } from "react";
import TodoList from "./TodoList";

jest.mock("../services/TaskService", () => ({
  fetchTasks: jest.fn(() =>
    Promise.resolve([
      {
        id: 1,
        name: "Sample Task",
        dueDate: "2025-04-15",
        priority: "High",
        flagDone: false,
        doneDate: null,
        creationDate: "2025-04-10",
      }
    ])
  ),
  markTaskDone: jest.fn(() => Promise.resolve()),
  markTaskUndone: jest.fn(() => Promise.resolve()),
  deleteTask: jest.fn(() => Promise.resolve()),
  createTask: jest.fn(() => Promise.resolve()),
  updateTask: jest.fn(() => Promise.resolve()),
}));

describe("TodoList", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it("renders the title and the Add New Task button", async () => {
    await act(async () => {
      render(<TodoList />);
    });
    expect(screen.getByText("Task Manager")).toBeInTheDocument();
    expect(screen.getByText("+ Add New Task")).toBeInTheDocument();
  });

  it("calls fetchTasks when applying a filter", async () => {
    const { fetchTasks } = require("../services/TaskService");

    await act(async () => {
      render(<TodoList />);
    });

    fireEvent.change(screen.getByLabelText("Name"), {
      target: { value: "Sample" },
    });

    fireEvent.click(screen.getByText("Search"));

    await waitFor(() => {
      expect(fetchTasks).toHaveBeenCalledWith(0, undefined, "name=Sample");
    });
  });
});
