package com.breakabletoy1.breakToy.sorts;

import com.breakabletoy1.breakToy.domain.ToDo;
import com.breakabletoy1.breakToy.helpers.ToDoControllerHelper;

import java.util.Comparator;

public class SortPrior implements Comparator<ToDo> {
    @Override
    public int compare(ToDo a, ToDo b) {
        return Integer.compare(
                ToDoControllerHelper.getPriorityValue(a.getPriority()),
                ToDoControllerHelper.getPriorityValue(b.getPriority())
        );
    }
}

