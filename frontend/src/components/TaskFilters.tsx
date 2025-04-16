import React from "react";
import "../styles/TodoStyles.css";


interface TaskFiltersProps {
  nameFilter: string;
  setNameFilter: (val: string) => void;
  priorFilter: string;
  setPriorFilter: (val: string) => void;
  stateFilter: string;
  setStateFilter: (val: string) => void;
  onApplyFilter: () => void;
}



const TaskFilters: React.FC<TaskFiltersProps> = ({
  nameFilter,
  setNameFilter,
  priorFilter,
  setPriorFilter,
  stateFilter,
  setStateFilter,
  onApplyFilter,
}) => {
  const cleanFilter =() => {
    setNameFilter("");
    setPriorFilter("");
    setStateFilter("");
  };

return(
  <div className="filters-container">
  <div className="filter-group">
    <label htmlFor="name">Name</label>
    <input
      type="text"
      id="name"
      value={nameFilter}
      onChange={(e) => setNameFilter(e.target.value)}
    />
  </div>

  <div className="filter-group">
    <label htmlFor="priority">Priority</label>
    <select
      id="priority"
      value={priorFilter}
      onChange={(e) => setPriorFilter(e.target.value)}
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
      value={stateFilter}
      onChange={(e) => setStateFilter(e.target.value)}
    >
      <option value="">All</option>
      <option value="Done">Done</option>
      <option value="Undone">Undone</option>
    </select>
  </div>

  <div className="filter-button">
    <button onClick={onApplyFilter}>Search</button>
  </div>
  <div className="clean-button">
    <button onClick={cleanFilter}>Clean</button>
  </div>
</div>

);
}

export default TaskFilters;