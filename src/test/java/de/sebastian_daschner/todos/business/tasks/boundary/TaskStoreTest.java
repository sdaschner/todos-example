package de.sebastian_daschner.todos.business.tasks.boundary;

import de.sebastian_daschner.todos.business.tasks.control.TaskCache;
import de.sebastian_daschner.todos.business.tasks.entity.Filter;
import de.sebastian_daschner.todos.business.tasks.entity.Task;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class TaskStoreTest {

    private TaskStore cut;

    @Before
    public void setUp() {
        cut = new TaskStore();
        cut.cache = Mockito.mock(TaskCache.class);

        Mockito.when(cut.cache.getAll()).thenReturn(sampleTasks());
    }

    @Test
    public void testUpdate() {
        final Task task = sampleTasks().iterator().next();
        cut.save(task);

        Assert.assertNotNull(task.getUpdated());
    }

    @Test
    public void testFilterContextMatch() {
        Filter filter = new Filter();
        filter.setContexts(new HashSet<>(Arrays.asList("abc")));
        List<Task> filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(1, filteredTasks.size());
        Assert.assertEquals(2L, filteredTasks.iterator().next().getId().longValue());
    }

    @Test
    public void testFilterContextNoMatch() {
        Filter filter = new Filter();
        filter.setContexts(new HashSet<>(Arrays.asList("abcd")));
        List<Task> filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(0, filteredTasks.size());
    }

    @Test
    public void testFilterNameMatch() {
        Filter filter = new Filter();
        filter.setText("test");
        List<Task> filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(3, filteredTasks.size());
        assertContainsExactly(filteredTasks, 1L, 2L, 3L);
    }

    @Test
    public void testFilterNameMatchUpperCase() {
        Filter filter = new Filter();
        filter.setText("teSt");
        List<Task> filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(3, filteredTasks.size());
        assertContainsExactly(filteredTasks, 1L, 2L, 3L);
    }

    @Test
    public void testFilterNameMatchUnicode() {
        Filter filter = new Filter();
        filter.setText("täSt");
        List<Task> filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(2, filteredTasks.size());
        assertContainsExactly(filteredTasks, 5L, 6L);
    }

    @Test
    public void testMultipleNameContexts() {
        Filter filter = new Filter();
        filter.setText("tEst");
        filter.setContexts(new HashSet<>(Arrays.asList("abc")));
        List<Task> filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(1, filteredTasks.size());
        Assert.assertEquals(2L, filteredTasks.iterator().next().getId().longValue());
    }

    @Test
    public void testFilterNameContextNoMatch() {
        Filter filter = new Filter();
        filter.setText("test");
        filter.setContexts(new HashSet<>(Arrays.asList("abcd")));
        List<Task> filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(0, filteredTasks.size());
    }

    @Test
    public void testFilterPriority() {
        Filter filter = new Filter();
        filter.setPriorityThreshold(1);
        List<Task> filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(3, filteredTasks.size());
        assertContainsExactly(filteredTasks, 2L, 4L, 6L);

        filter.setPriorityThreshold(2);
        filteredTasks = cut.filterAll(filter);

        Assert.assertEquals(1, filteredTasks.size());
        assertContainsExactly(filteredTasks, 6L);
    }

    private List<Task> sampleTasks() {
        Task firstTask = createTask("test123", 1L, null);
        Task secondTask = createTask("anotherTest234", 2L, 1, "abc", "efg");
        Task thirdTask = createTask("abctEsT42", 3L, null);
        Task fourthTask = createTask("another157", 4L, 1, "cde");
        Task fifthTask = createTask("täst", 5L, null);
        Task sixthTask = createTask("anothertÄst", 6L, 2, "efg");

        return Arrays.asList(firstTask, secondTask, thirdTask, fourthTask, fifthTask, sixthTask);
    }

    private Task createTask(final String name, final long id, final Integer priority, final String... contexts) {
        final Task task = new Task();
        task.setId(id);
        task.setName(name);
        task.setPriority(priority);
        task.setContexts(new HashSet<>(Arrays.asList(contexts)));
        return task;
    }

    private void assertContainsExactly(final List<Task> filteredTasks, final Long... ids) {
        List<Long> idsLeft = Arrays.stream(ids).collect(Collectors.toList());

        for (final Task task : filteredTasks) {
            Assert.assertTrue(idsLeft.remove(task.getId()));
        }
        Assert.assertTrue(idsLeft.isEmpty());
    }

}
