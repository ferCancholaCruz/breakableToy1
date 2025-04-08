import React, { useState } from "react";

interface DatosForm {
  name: string;
  dueDate: string;
  priority: string;
}

interface TodoFormProps {
  modo: "crear" | "editar";
  valoresIniciales?: DatosForm;
  onSubmit: (datos: DatosForm) => void;
}

const TodoForm = ({ modo, valoresIniciales, onSubmit }: TodoFormProps) => {
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
      alert("El nombre es obligatorio y no debe tener más de 120 caracteres.");
      return;
    }
    onSubmit(datos);
    if (modo === "crear") {
      setDatos({ name: "", dueDate: "", priority: "" });
    }
  }

  return (
    <form onSubmit={handleSubmit}>
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
        <option value="">Seleccionar</option>
        <option value="High">High</option>
        <option value="Medium">Medium</option>
        <option value="Low">Low</option>
      </select>
      <button type="submit">
        {modo === "crear" ? "Crear tarea" : "Guardar cambios"}
      </button>
    </form>
  );
};

export default TodoForm;
