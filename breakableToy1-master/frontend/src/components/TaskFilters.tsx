import React, { useState } from "react";
import "../styles/TodoStyles.css";
import { TaskFiltersProps } from "../Types";

const TaskFilters: React.FC<TaskFiltersProps> = ({
  setNameFilter,
  setPriorFilter,
  setStateFilter,
  onApplyFilter,
}) => {
  const [localName, setLocalName] = useState("");
  const [localPriority, setLocalPriority] = useState("");
  const [localState, setLocalState] = useState("");

  const handleSearchClick = () => {
    setNameFilter(localName);
    setPriorFilter(localPriority);
    setStateFilter(localState);
    onApplyFilter();
  };

  const cleanFilter = () => {
    setLocalName("");
    setLocalPriority("");
    setLocalState("");
    setNameFilter("");
    setPriorFilter("");
    setStateFilter("");
    onApplyFilter();
  };

  return (
    <div className="filters-container">
      <div className="filter-group">
        <label htmlFor="name">Name</label>
        <input
          type="text"
          id="name"
          value={localName}
          onChange={(e) => setLocalName(e.target.value)}
        />
      </div>

      <div className="filter-group">
        <label htmlFor="priority">Priority</label>
        <select
          id="priority"
          value={localPriority}
          onChange={(e) => setLocalPriority(e.target.value)}
        >
          <option value="">All</option>
          <option value="High">High</option>
          <option value="Medium">Medium</option>
          <option value="Low">Low</option>
        </select>
      </div>

      <div className="filter-group">
        <label htmlFor="status">Task Status</label>
        <select
          id="status"
          value={localState}
          onChange={(e) => setLocalState(e.target.value)}
        >
          <option value="">All</option>
          <option value="Done">Done</option>
          <option value="Undone">Undone</option>
        </select>
      </div>

      <div className="filter-button">
        <button onClick={handleSearchClick}>Search</button>
      </div>
      <div className="clean-button">
        <button onClick={cleanFilter}>Clean</button>
      </div>
    </div>
  );
};

export default TaskFilters;
