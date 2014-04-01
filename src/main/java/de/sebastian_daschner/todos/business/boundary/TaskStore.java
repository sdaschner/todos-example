package de.sebastian_daschner.todos.business.boundary;

import de.sebastian_daschner.todos.business.tasks.entity.Task;

import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TaskStore {

    private Map<Long, Task> tasks = new ConcurrentHashMap<>();

    AtomicLong next = new AtomicLong(0L);

    public List<Task> listAll() {
        return new LinkedList<>(tasks.values());
    }

    public Task get(long taskId) {
        return tasks.get(taskId);
    }

    public long save(Task task) {
        final long taskId = next.getAndIncrement();
        tasks.put(taskId, task);

        return taskId;
    }

    public void update(long taskId, Task task) {
        tasks.put(taskId, task);
    }

    public void delete(long taskId) {
        tasks.remove(taskId);
    }

}
