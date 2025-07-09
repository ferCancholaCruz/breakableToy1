package com.breakabletoy1.breakToy.helpers;

public class ToDoControllerHelper {

    //priority from a word to a numeric
    public static int getPriorityValue(String priority) {
        return switch (priority) {
            case "High" -> 1;
            case "Medium" -> 2;
            case "Low" -> 3;
            default -> 4;
        };
    }
}

