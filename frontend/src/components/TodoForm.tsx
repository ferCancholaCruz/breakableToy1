import React, { useState } from "react";
import "../styles/TodoStyles.css";

interface DatosForm {
  name: string;
  dueDate: string;
  priority: string;
}

interface TodoFormProps {
  modo: "crear" | "editar";
  valoresIniciales?: DatosForm;
  onSubmit: (datos: DatosForm) => void;
  onCancel?: () => void;
}

const TodoForm = ({ modo, valoresIniciales, onSubmit, onCancel }: TodoFormProps) => {
  const [datos, setDatos] = useState<DatosForm>(
    valoresIniciales || {
      name: "",
      dueDate: "",
      priority: "",
    }
  );

  function handleChange(e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) {
    const { name, value } = e.target;
    setDatos((prev) => ({ ...prev, [name]: value }));
  }

  function handleSubmit(e: React.FormEvent<HTMLFormElement>) {
    e.preventDefault();
    if (!datos.name || datos.name.length > 120) {
      alert("Name is necessary or it cant be more than 120 letters");
      return;
    }

    if (datos.priority === "") {
      alert("Please select a priority before submitting.");
      return; 
    }
    onSubmit(datos);
    if (modo === "crear") {
      setDatos({ name: "", dueDate: "", priority: "" });
      
    }
  }

  return (
    <form onSubmit={handleSubmit} className="todo-form">
      <input
        name="name"
        type="text"
        placeholder="Nombre"
        value={datos.name}
        onChange={handleChange}
        required
      />
      <input
        name="dueDate"
        type="date"
        value={datos.dueDate}
        onChange={handleChange}
      />
      <select
        name="priority"
        value={datos.priority}
        onChange={handleChange}
      >
        <option value="">Priority</option>
        <option value="High">High</option>
        <option value="Medium">Medium</option>
        <option value="Low">Low</option>
      </select>
      <button type="submit">
        {modo === "crear" ? "Create task" : "Save changes"}
      </button>
      <button type="button" onClick={onCancel}>
        Cancel
       
      </button>
    </form>
  );
};

export default TodoForm;
