import { render, screen, fireEvent } from "@testing-library/react";
import React from "react";
import TaskFilters from "./TaskFilters";

test("calls the appropriate handlers when filters are used", () => {
  const mockSetNameFilter = jest.fn();
  const mockSetPriorFilter = jest.fn();
  const mockSetStateFilter = jest.fn();
  const mockApplyFilter = jest.fn();

  render(
    <TaskFilters
      nameFilter=""
      setNameFilter={mockSetNameFilter}
      priorFilter=""
      setPriorFilter={mockSetPriorFilter}
      stateFilter=""
      setStateFilter={mockSetStateFilter}
      onApplyFilter={mockApplyFilter}
    />
  );

  const searchButton = screen.getByText("Search");
  fireEvent.click(searchButton);
  expect(mockApplyFilter).toHaveBeenCalled();

  const input = screen.getByLabelText("Name");
  fireEvent.change(input, { target: { value: "meeting" } });
  expect(mockSetNameFilter).toHaveBeenCalledWith("meeting");

  const prioritySelect = screen.getByLabelText("Priority");
  fireEvent.change(prioritySelect, { target: { value: "High" } });
  expect(mockSetPriorFilter).toHaveBeenCalledWith("High");

  const statusSelect = screen.getByLabelText("Task Status");
  fireEvent.change(statusSelect, { target: { value: "Done" } });
  expect(mockSetStateFilter).toHaveBeenCalledWith("Done");
});



test("call setPriorFilter when selecting a priority", () => {
    const mockSetPriorFilter = jest.fn();
  
    render(
      <TaskFilters
        nameFilter=""
        setNameFilter={() => {}}
        priorFilter=""
        setPriorFilter={mockSetPriorFilter}
        stateFilter=""
        setStateFilter={() => {}}
        onApplyFilter={() => {}}
      />
    );
  
    const select = screen.getByLabelText("Priority");
    fireEvent.change(select, { target: { value: "High" } });
  
    expect(mockSetPriorFilter).toHaveBeenCalledWith("High");
  });



  test("renders all filter fields", () => {
    render(
      <TaskFilters
        nameFilter=""
        setNameFilter={jest.fn()}
        priorFilter=""
        setPriorFilter={jest.fn()}
        stateFilter=""
        setStateFilter={jest.fn()}
        onApplyFilter={jest.fn()}
      />
    );
  
    expect(screen.getByLabelText("Name")).toBeInTheDocument();
    expect(screen.getByLabelText("Priority")).toBeInTheDocument();
    expect(screen.getByLabelText("Task Status")).toBeInTheDocument();
  });
  
  