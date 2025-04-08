package com.breakabletoy1.breakToy.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import com.breakabletoy1.breakToy.domain.ToDo;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.*;

// Use english for everthing, it is weird to have english and spanish combined and since you cannot change Java to spanish, lets use english for everything
@RestController
public class ToDoController {
    // saving state in a controller is not a good idea
    // what about the service layer? the repository layer?
    LocalDate fecha1=LocalDate.parse("2025-02-10");
    LocalDate fecha2=LocalDate.parse("2025-02-20");
    LocalDate fecha3=LocalDate.parse("2025-02-10");


    public List<ToDo> acts = new ArrayList<>(Arrays.asList(
            new ToDo(123, "Tarea de ejemplo 1", true, "High", fecha1, fecha2)

    ));

    // this method is too big, try refactor the method and split its logic in other methods
    @GetMapping("/todos") //mostar actividades
    public List<ToDo> getActs(
            @RequestParam (defaultValue="0")int ind1,
            @RequestParam (required=false) String orden,
            @RequestParam (defaultValue="false") Boolean done,
            @RequestParam (required=false) String nombre,
            @RequestParam (required=false) String priority
    ){
        List<ToDo> filtradas = new ArrayList<>();

        for (ToDo t : acts) {
            filtradas.add(t);
        }

        //filtro de done u undone
        // if (done) is enough, since done is a boolean you dont need to compare it with true to know if it is true
        if (done==true){
            // try changing the logic here to use streams
             List<ToDo> doneT = new ArrayList<>();
            for (ToDo t: filtradas){
                // if (t.getFlag()) is enough
                // what is flag? what does it mean? use more "intention revealing" names
                if (t.getFlag()==true){
                    doneT.add(t);

                }
            }
            filtradas = doneT;

        } else if (done==false){ // if (!done) is enough
            List<ToDo> undoneT = new ArrayList<>();
            for (ToDo t: filtradas){
                if (t.getFlag()==false){ // if (!t.getFlag() is enough
                    undoneT.add(t);

                }
            }
            filtradas = undoneT;
        }

        //Fltro por nombre
        if (nombre!=null){
            List<ToDo> filtNombre = new ArrayList<>();
            for (ToDo t : filtradas){
                if(t.getName().contains(nombre)){
                    filtNombre.add(t);
                }
            }
            // what if the "done" filtering happened before? this logic is going to override the already filtered items, isn't it?
            filtradas = filtNombre;
        }

        //Filtro de prioridad
        // try reversing the comparation: "Low".equalsIgnoreCase(priority), can you guess why this way is better?
        if (priority!=null && priority.equalsIgnoreCase("Low")){
            List<ToDo> filtPrioridadL = new ArrayList<>();
            for (ToDo t : filtradas){
                if(t.getPriority().equalsIgnoreCase("Low")) {
                    filtPrioridadL.add(t);
                }
            }
            // same issue, what if you already filtered by done or name?
            filtradas = filtPrioridadL;
        } else if (priority!=null && priority.equalsIgnoreCase("Medium")){
            List<ToDo> filtPrioridadM = new ArrayList<>();
            for (ToDo t : filtradas){
                if(t.getPriority().equalsIgnoreCase("Medium")) {
                    filtPrioridadM.add(t);
                }
            }
            filtradas = filtPrioridadM;
        } else if (priority!=null && priority.equalsIgnoreCase("High")){
            List<ToDo> filtPrioridadH = new ArrayList<>();
            for (ToDo t : filtradas){
                if(t.getPriority().equalsIgnoreCase("High")) {
                    filtPrioridadH.add(t);
                }
            }
            filtradas = filtPrioridadH;
        }

        //return paginar(ind1, acts);

        //sort por prioridad, fecha o ambos
        if (orden != null) {
            switch (orden) {
                case "PriorityAsc" -> filtradas.sort(new SortPrior());
                case "PriorityDesc" -> filtradas.sort(Collections.reverseOrder(new SortPrior()));

                case "DueDateAsc" -> filtradas.sort(new SortDueDate());
                case "DueDateDesc" -> filtradas.sort(Collections.reverseOrder(new SortDueDate()));

                case "BothAsc" -> filtradas.sort(new SortAmbos());
                case "BothDesc" -> filtradas.sort(Collections.reverseOrder(new SortAmbos()));

                case "PriorityAscDueDesc" -> {
                    filtradas.sort((a, b) -> {
                        int p1 = numericoPrior(a.getPriority());
                        int p2 = numericoPrior(b.getPriority());
                        int comp = Integer.compare(p1, p2); // prioridad ascendente

                        if (comp == 0 && a.getDueDate() != null && b.getDueDate() != null) {
                            // Fecha descendente (la más reciente primero)
                            return b.getDueDate().compareTo(a.getDueDate());
                        }

                        return comp;
                    });
                }
                case "PriorityDescDueAsc" -> {
                    filtradas.sort((a, b) -> {
                        int p1 = numericoPrior(a.getPriority());
                        int p2 = numericoPrior(b.getPriority());
                        int comp = Integer.compare(p2, p1); // prioridad descendente

                        if (comp == 0 && a.getDueDate() != null && b.getDueDate() != null) {
                            return a.getDueDate().compareTo(b.getDueDate()); // fecha ascendente
                        }

                        return comp;
                    });
                }

            }
        }

        return paginar(ind1, filtradas);

    }

    public List<ToDo> paginar(int indice, List<ToDo> lista){
        int tamanoPag =10; // this is a constant, it shouldn't be declared here
        int inicio= tamanoPag * indice;
        // is that 10 also the pageSize? if not, what is it?
        int fin=Math.min(inicio+10, lista.size());

        if (inicio >= lista.size()){
            return List.of();
        }

        return lista.subList(inicio, fin);
    }

    @PostMapping("/todos")   //crear to dos
    public ToDo postToDo(@RequestBody ToDo todo){
        int nuevoID = acts.isEmpty() ? 1 : acts.get(acts.size() - 1).getID() + 1;
        todo.setID(nuevoID);
        todo.setCreationDate(LocalDate.now());
        acts.add(todo);
        return todo;

    }

    @PutMapping("/todos/{id}")
    public ToDo editToDo(@RequestBody ToDo actividad, @PathVariable int id){
        // what if instead you use streams to find the activity you need to edit?
        for (ToDo act : acts){
            if (act.getID()==id){
                if (actividad.getName() == null || actividad.getName().length() > 120) {
                    throw new IllegalArgumentException("El nombre no puede ser nulo ni mayor a 120 caracteres");
                }

                act.setName(actividad.getName());
                act.setDueDate(actividad.getDueDate());
                act.setPriority(actividad.getPriority());
                return act;
            }
        }
        return null;
    }


    //POST to mark "to do" as bone
    @PostMapping("/todos/{id}/done")
    public ToDo markDone(@PathVariable int id){
        // what if instead you use streams to find the activity you need to mark as done?
        for (ToDo act : acts){
            if (act.getID()==id){
                act.setFlag(true);
                act.setDoneDate(LocalDate.now());

                return act;
            }
        }
        throw new NoSuchElementException("No hay tarea con ID " + id);
    }

    @PostMapping("/todos/{id}/undone")
    public ToDo markUndone(@PathVariable int id){

        for (ToDo act : acts){
            if (act.getID()==id){
                act.setFlag(false);

                return act;
            }
        }
        // here you are throwing an exception when you cannot find the activity, but in the edit logic you are just
        // returning null, what do you think should be done in both to be consistent?
        throw new NoSuchElementException("No hay tarea con ID " + id);
    }
    public int numericoPrior (String prioridad){
        return switch(prioridad) {
            case "High" -> 1;
            case "Medium" -> 2;
            case "Low" -> 3;
            default -> 4;
        };
    }

}

class SortAmbos implements Comparator<ToDo>{
    @Override
    public int compare(ToDo act1, ToDo act2){
        int p1= numericoPrior(act1.getPriority());
        int p2= numericoPrior(act2.getPriority());

        int compararPrior = Integer.compare(p1, p2);

        if(compararPrior==0 && act1.getDueDate() != null && act2.getDueDate() != null){
            return act1.getDueDate().compareTo(act2.getDueDate());
        }

        return compararPrior;
    }

    // this is repeated code, line 230 has the same method
    public int numericoPrior (String prioridad){
        return switch(prioridad) {
            case "High" -> 1;
            case "Medium" -> 2;
            case "Low" -> 3;
            default -> 4;
        };
    }
}

class SortPrior implements Comparator<ToDo>{
    @Override
    public int compare(ToDo act1, ToDo act2){
        int p1= numericoPrior(act1.getPriority());
        int p2= numericoPrior(act2.getPriority());

        int compararPrior = Integer.compare(p1, p2);

        return compararPrior;
    }

    // this is repeated code, line 230 has the same method
    public int numericoPrior (String prioridad){
        return switch(prioridad) {
            case "High" -> 1;
            case "Medium" -> 2;
            case "Low" -> 3;
            default -> 4;
        };
    }
}

class SortDueDate implements Comparator<ToDo>{
    @Override
    public int compare(ToDo act1, ToDo act2){
        if (act1.getDueDate() == null || act2.getDueDate() == null) {
            return 0;
        }
        return act1.getDueDate().compareTo(act2.getDueDate());
    }
}

