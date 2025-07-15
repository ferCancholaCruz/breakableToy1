package com.breakabletoy1.breakToy.sorts;

import com.breakabletoy1.breakToy.domain.ToDo;
import com.breakabletoy1.breakToy.helpers.ToDoControllerHelper;

import java.util.Comparator;

public class SortBoth implements Comparator<ToDo> {
    @Override
    public int compare(ToDo a, ToDo b) {
        int cmp = Integer.compare(
                ToDoControllerHelper.getPriorityValue(a.getPriority()),
                ToDoControllerHelper.getPriorityValue(b.getPriority())
        );

        if (cmp == 0 && a.getDueDate() != null && b.getDueDate() != null) {
            return a.getDueDate().compareTo(b.getDueDate());
        }

        return cmp;
    }
}
