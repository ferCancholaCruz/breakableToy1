package com.breakabletoy1.breakToy.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "todos")
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 120, nullable = false)
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    private boolean flagDone;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate doneDate;

    private String priority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;

    public ToDo() {
    }

    public ToDo(Long id, String name, boolean flagDone, String priority, LocalDate creationDate, LocalDate doneDate) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.flagDone = flagDone;
        this.priority = priority;
        this.creationDate = creationDate;
        this.doneDate = doneDate;
    }

    // ✅ Validación centralizada
    private void validateName(String name) {
        if (name == null || name.length() > 120) {
            throw new IllegalArgumentException("Name can't be empty or more than 120 letters");
        }
    }

    public Long getID() {
        return id;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        validateName(name);
        this.name = name;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public boolean getFlagDone() {
        return flagDone;
    }

    public void setFlagDone(boolean flagDone) {
        this.flagDone = flagDone;
    }

    public LocalDate getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDate doneDate) {
        this.doneDate = doneDate;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
