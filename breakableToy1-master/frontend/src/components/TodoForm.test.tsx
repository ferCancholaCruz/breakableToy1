import React from "react";
import TodoForm from "./TodoForm";
import { render, screen, fireEvent } from "@testing-library/react";

beforeEach(() => {
    window.alert = jest.fn(); 
  });  

// verify that the TodoForm can only be submitted if the required fields are completed
describe("TodoForm validation", () => {
    test("does not submit when name is empty", () => {
      const mockSubmit = jest.fn();
  
      render(<TodoForm modo="crear" onSubmit={mockSubmit} />);
  
      const submitButton = screen.getByRole("button", { name: /create task/i });
      fireEvent.click(submitButton);
  
      expect(mockSubmit).not.toHaveBeenCalled();
    });
  
    test("does not submit when priority is not selected", () => {
      const mockSubmit = jest.fn();
  
      render(<TodoForm modo="crear" onSubmit={mockSubmit} />);
  
      // Rellenamos el nombre pero no la prioridad
      const nameInput = screen.getByPlaceholderText(/nombre/i);
      fireEvent.change(nameInput, { target: { value: "Sample Task" } });
  
      const submitButton = screen.getByRole("button", { name: /create task/i });
      fireEvent.click(submitButton);
  
      expect(mockSubmit).not.toHaveBeenCalled();
    });
  
    test("submits when name and priority are filled", () => {
      const mockSubmit = jest.fn();
  
      render(<TodoForm modo="crear" onSubmit={mockSubmit} />);
  
      fireEvent.change(screen.getByPlaceholderText(/nombre/i), {
        target: { value: "Task A" },
      });
  
      fireEvent.change(screen.getByRole("combobox"), {
        target: { value: "High" },
      });
  
      const submitButton = screen.getByRole("button", { name: /create task/i });
      fireEvent.click(submitButton);
  
      expect(mockSubmit).toHaveBeenCalledWith({
        name: "Task A",
        dueDate: "",
        priority: "High",
      });
    });
  });



  describe("TodoForm edit mode", () => {
    test("shows initial values and submits updated task", () => {
      const mockSubmit = jest.fn();
  
      render(
        <TodoForm
          modo="editar"
          valoresIniciales={{ name: "Testcreate", dueDate: "2025-05-01", priority: "Medium" }}
          onSubmit={mockSubmit}
        />
      );
  
      expect(screen.getByDisplayValue("Testcreate")).toBeInTheDocument();
      expect(screen.getByDisplayValue("2025-05-01")).toBeInTheDocument();
      expect(screen.getByDisplayValue("Medium")).toBeInTheDocument();
  
      fireEvent.change(screen.getByPlaceholderText(/nombre/i), {
        target: { value: "Updated Task" },
      });
  
      const saveButton = screen.getByRole("button", { name: /save changes/i });
      fireEvent.click(saveButton);
  
      expect(mockSubmit).toHaveBeenCalledWith({
        name: "Updated Task",
        dueDate: "2025-05-01",
        priority: "Medium",
      });
    });
  });