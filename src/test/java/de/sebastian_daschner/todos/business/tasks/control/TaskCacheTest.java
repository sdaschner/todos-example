package de.sebastian_daschner.todos.business.tasks.control;


import de.sebastian_daschner.todos.business.tasks.entity.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;

public class TaskCacheTest {

    private TaskCache cut;

    @Before
    public void setUp() {
        cut = new TaskCache();
    }

    @Test
    public void testCrud() {
        Assert.assertTrue(cut.getAll().isEmpty());
        Task firstTask = createTask();
        Long firstId = cut.store(firstTask);

        List<Task> firstTasks = cut.getAll();
        Assert.assertEquals(1, firstTasks.size());
        Assert.assertEquals(firstId, firstTasks.iterator().next().getId());
        Assert.assertEquals(firstTask, cut.get(firstId));
        Assert.assertEquals(firstId, cut.get(firstId).getId());

        Task secondTask = createTask();
        Long secondId = cut.store(secondTask);

        List<Task> secondTasks = cut.getAll();
        Assert.assertEquals(2, secondTasks.size());
        Assert.assertEquals(secondTask, cut.get(secondId));
        Assert.assertEquals(secondId, cut.get(secondId).getId());
    }

    @Test
    public void testSort() {
        final Date januaryFirst = Date.from(LocalDate.of(2014, Month.JANUARY, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Date februarySecond = Date.from(LocalDate.of(2014, Month.FEBRUARY, 2).atStartOfDay(ZoneId.systemDefault()).toInstant());
        final Date marchFirst = Date.from(LocalDate.of(2014, Month.MARCH, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        final List<Long> expectedOrder = Arrays.asList(5L, 3L, 6L, 7L, 1L, 4L, 2L);

        cut.store(createTask(1L, "Test", januaryFirst, false, null));
        cut.store(createTask(2L, "Another test", februarySecond, true, null));
        cut.store(createTask(3L, "Hello Test", marchFirst, false, 1));
        cut.store(createTask(4L, "hello", januaryFirst, true, 2));
        cut.store(createTask(5L, "hello test", februarySecond, false, 3));
        cut.store(createTask(6L, "hello test", februarySecond, false, null));
        cut.store(createTask(7L, "hello test1", februarySecond, false, null));

        List<Task> tasks = cut.getAll();

        Iterator<Long> orders = expectedOrder.iterator();
        for (final Task task : tasks) {
            final Long id = orders.next();
            Assert.assertEquals(id, task.getId());
        }
    }

    private Task createTask(final long id, final String name, final Date updated, final boolean finished, final Integer priority) {
        final Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setUpdated(updated);
        task.setFinished(finished);
        task.setPriority(priority);
        return task;
    }

    private Task createTask() {
        final Task task = new Task();
        task.setName("sample");
        task.setContexts(new HashSet<>(Arrays.asList("abcd")));
        return task;
    }

}
