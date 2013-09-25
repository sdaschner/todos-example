package de.sebastian_daschner.todos.ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import de.sebastian_daschner.todos.model.Category;
import de.sebastian_daschner.todos.model.Task;
import de.sebastian_daschner.todos.model.TaskManagerException;

@Stateless
public class TaskManagerBean {

	@PersistenceContext
	EntityManager entityManager;

	public List<Task> getAllTasks() {
		return entityManager.createQuery("SELECT t FROM Task t", Task.class)
				.getResultList();
	}

	public void addTask(final Task task) throws TaskManagerException {
		try {
			entityManager.persist(task);
		} catch (final PersistenceException e) {
			throw new TaskManagerException("could not add task", e);
		}
	}

	public void deleteTask(final Long id) throws TaskManagerException {
		final Task task = entityManager.find(Task.class, id);
		if (task == null) {
			throw new TaskManagerException("could not find task with id: " + id);
		}
		deleteTask(task);
	}

	public void deleteTask(final Task task) throws TaskManagerException {
		try {
			entityManager.remove(task);
		} catch (final PersistenceException e) {
			throw new TaskManagerException("could not delete task", e);
		}
	}

	public List<Task> getTasks(final Category category) {
		return null;
	}
}
