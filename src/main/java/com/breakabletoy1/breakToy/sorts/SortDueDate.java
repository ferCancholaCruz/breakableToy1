package com.breakabletoy1.breakToy.sorts;

import com.breakabletoy1.breakToy.domain.ToDo;

import java.util.Comparator;

public class SortDueDate implements Comparator<ToDo> {
    @Override
    public int compare(ToDo a, ToDo b) {
        if (a.getDueDate() == null || b.getDueDate() == null) {
            return 0;
        }
        return a.getDueDate().compareTo(b.getDueDate());
    }
}

