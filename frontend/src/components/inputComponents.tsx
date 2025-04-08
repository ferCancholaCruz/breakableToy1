import React from "react";
import {useState} from "react";

interface DatosForm {
  name: string;
  dueDate: string;
  priority: string;
}

const TodoForm = () => {
  const [datos, setDatos] = useState<DatosForm>({
    name: '',
    dueDate: '',
    priority: ''
  });

  function cambiarName(t: React.ChangeEvent<HTMLInputElement>) {
    setDatos((data) => ({
      ...data,
      name: t.target.value
    }));
  }

  function cambiarDueDate(t: React.ChangeEvent<HTMLInputElement>) {
    setDatos((data) => ({
      ...data,
      dueDate: t.target.value
    }));
  }

  function cambiarPriority(t: React.ChangeEvent<HTMLSelectElement>) {
    setDatos((data) => ({
      ...data,
      priority: t.target.value
    }));
  }

  async function handleSubmit(e: React.FormEvent<HTMLFormElement>){
    e.preventDefault();
    try {
      const res =await fetch("/todos", {
        method: "POST",
        headers: {"Content-Type": "application/json"},   //send data in json format
        body: JSON.stringify({  //converte to json
          ...datos,  //data from forms
          flag: false
        })
      })

      const nueva = await res.json(); //get the backend response 
      console.log("Tarea creada:", nueva);
      setDatos({
        name: "",
        dueDate: "",
        priority: ""
      });
    } catch (err){
      console.error("Fail at creating task", err);
    }


  }

  return (
    <form onSubmit={handleSubmit}>

      <input
        type="text"
        placeholder="Nombre"
        value={datos.name}
        onChange={cambiarName}
      />
  
      <input
        type="date"
        value={datos.dueDate}
        onChange={cambiarDueDate}
      />
  
      <label htmlFor="prior">Prioridad</label>
      <select
        name="priority"
        id="prior"
        value={datos.priority}
        onChange={cambiarPriority}
      >
        <option value="High">High</option>
        <option value="Medium">Medium</option>
        <option value="Low">Low</option>
      </select>
  
      <button type="submit">Crear tarea</button>
    </form>
  );
}



export default TodoForm;
  
