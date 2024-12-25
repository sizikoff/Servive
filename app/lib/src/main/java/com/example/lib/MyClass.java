package com.example.lib;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class MyClass {

    public static void main(String[] args) {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task("A",3));
        tasks.add(new Task("B",1));
        tasks.add(new Task("C",5));
        tasks.add(new Task("D",2));
        tasks.add(new Task("E",4));

        int minPriority = 2;

        Iterator<Task> iterator = tasks.iterator();
        while (iterator.hasNext()){
            Task task = iterator.next();
            if (task.priority <minPriority) {
                iterator.remove();
            }
        }
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task t1, Task t2) {
                return Integer.compare(t1.priority,t2.priority);
            }
        });

        System.out.println(tasks);
    }
}