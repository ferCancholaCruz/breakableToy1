export interface Task {
    id: number;
    name: string;
    dueDate: string;
    priority: string;
    done: boolean;
    doneDate: string | null;
    creationDate: string;
  }
  
  export interface TaskFormData {
    name: string;
    dueDate: string;
    priority: string;
  }
  

 export interface Task {
    id: number;
    name: string;
    dueDate: string;
    priority: string;
    done: boolean;
    doneDate: string | null;
    creationDate: string;
  }
  
  export interface TaskTableProps {
    tasks: Task[];
    editandoId: number | null;
    setEditandoId: (id: number | null) => void;
    flagDone: (id: number, done: boolean) => void;
    deleteAct: (id: number) => void;
    editarTarea: (id: number, data: any) => void;
    toggleSort: (col: "Priority" | "DueDate") => void;
    priorityArrow: "none" | "asc" | "desc";
    dueArrow: "none" | "asc" | "desc";
    averageHigh: number;
    averageMedium: number;
    averageLow: number;
    averageAll: number;
    onCheckAll: () => void;
    onUncheckAll: () => void;
  }

  export interface TaskFiltersProps {
    nameFilter: string;
    setNameFilter: (val: string) => void;
    priorFilter: string;
    setPriorFilter: (val: string) => void;
    stateFilter: string;
    setStateFilter: (val: string) => void;
    onApplyFilter: () => void;
  }