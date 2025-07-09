import { useState } from "react";

export function useTaskFilters() {
  const [name, setName] = useState("");
  const [priority, setPriority] = useState("");
  const [status, setStatus] = useState("");

  const buildFilterString = () => {
    const filters = [];
    if (name) filters.push(`name=${name}`);
    if (priority) filters.push(`priority=${priority}`);
    if (status === "Done") filters.push("done=true");
    else if (status === "Undone") filters.push("done=false");
    return filters.join("&");
  };

  return {
    name, setName,
    priority, setPriority,
    status, setStatus,
    buildFilterString,
  };
}
