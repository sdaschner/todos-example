package de.sebastian_daschner.todos.business.tasks.control;

import de.sebastian_daschner.todos.business.tasks.entity.Task;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SessionScoped
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TaskCache implements Serializable {

    private final Map<Long, Task> tasks;

    private final AtomicLong nextTask;

    private final Comparator<Task> comparator;

    public TaskCache() {
        tasks = new ConcurrentHashMap<>();
        nextTask = new AtomicLong(1);
        comparator = Comparator
                .comparing(Task::isFinished)
                .thenComparing(Comparator.comparing(Task::getPriority, Comparator.nullsFirst(Integer::compareTo)).reversed())
                .thenComparing(Comparator.comparing(Task::getUpdated, Comparator.nullsFirst(Date::compareTo)).reversed())
                .thenComparing(Task::getName);
    }

    public List<Task> getAll() {
        final LinkedList<Task> allTasks = new LinkedList<>(tasks.values());

        // sorted by finished asc, priority desc, updated desc, name asc
        Collections.sort(allTasks, comparator);

        return allTasks;
    }

    public Task get(final Long id) {
        return tasks.get(id);
    }

    public Long store(final Task task) {
        final Long id;
        if (task.getId() == null) {
            id = nextTask.getAndIncrement();
            task.setId(id);
        } else {
            id = task.getId();
        }
        tasks.put(id, task);
        return id;
    }

    public void remove(final Long id) {
        tasks.remove(id);
    }

}
