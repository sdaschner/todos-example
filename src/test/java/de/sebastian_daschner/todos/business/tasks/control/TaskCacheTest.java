package de.sebastian_daschner.todos.business.tasks.control;


import de.sebastian_daschner.todos.business.tasks.entity.Task;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class TaskCacheTest {

    private TaskCache cut;

    @Before
    public void setUp() {
        cut = new TaskCache();
    }

    @Test
    public void test() {
        Assert.assertTrue(cut.getAll().isEmpty());
        Task firstTask = sampleTask();
        Long firstId = cut.store(firstTask);

        List<Task> firstTasks = cut.getAll();
        Assert.assertEquals(1, firstTasks.size());
        Assert.assertEquals(firstId, firstTasks.iterator().next().getId());
        Assert.assertEquals(firstTask, cut.get(firstId));
        Assert.assertEquals(firstId, cut.get(firstId).getId());

        Task secondTask = sampleTask();
        Long secondId = cut.store(secondTask);

        List<Task> secondTasks = cut.getAll();
        Assert.assertEquals(2, secondTasks.size());
        Assert.assertEquals(secondTask, cut.get(secondId));
        Assert.assertEquals(secondId, cut.get(secondId).getId());
    }

    private Task sampleTask() {
        final Task task = new Task();
        task.setName("sample");
        task.setContexts(new HashSet<>(Arrays.asList("abcd")));
        return task;
    }

}
