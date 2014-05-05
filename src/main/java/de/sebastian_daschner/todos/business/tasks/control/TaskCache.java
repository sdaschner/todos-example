package de.sebastian_daschner.todos.business.tasks.control;

import de.sebastian_daschner.todos.business.tasks.entity.Task;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@SessionScoped
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TaskCache implements Serializable {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();

    private final AtomicLong nextTask = new AtomicLong(1);

    public List<Task> getAll() {
        return new LinkedList<>(tasks.values());
    }

    public Task get(final Long id) {
        return tasks.get(id);
    }

    public Long store(final Task task) {
        final Long id;
        if (task.getId() == null) {
            id = nextTask.getAndIncrement();
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
