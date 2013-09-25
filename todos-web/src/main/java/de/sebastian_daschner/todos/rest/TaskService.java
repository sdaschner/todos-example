package de.sebastian_daschner.todos.rest;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import de.sebastian_daschner.todos.ejb.TaskManagerBean;
import de.sebastian_daschner.todos.model.Task;
import de.sebastian_daschner.todos.model.TaskManagerException;
import de.sebastian_daschner.todos.rest.model.StatusResponse;
import de.sebastian_daschner.todos.rest.model.TaskBeanParam;

/**
 * JAX-RS Example
 *
 * This class produces a RESTful service to read the contents of the members
 * table.
 */
@Path("/todos")
@RequestScoped
public class TaskService {

	@EJB
	TaskManagerBean taskManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Task> getAllTasks() {

		return taskManager.getAllTasks();
	}

	@GET
	@Path("/add")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusResponse addTask(@BeanParam final TaskBeanParam taskBeanParam) {

		final Task task = new Task();
		task.setDescription(taskBeanParam.getDescription());
		task.setDueDate(taskBeanParam.getDueDate());
		task.setCreationDate(new Date());

		try {
			taskManager.addTask(task);
		} catch (final TaskManagerException e) {
			return StatusResponse.error(e.getMessage());
		}

		return StatusResponse.success();
	}

	@GET
	@Path("/delete/{id:[0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public StatusResponse deleteTask(@PathParam("id") final Long id) {
		try {
			taskManager.deleteTask(id);
		} catch (final TaskManagerException e) {
			return StatusResponse.error(e.getMessage());
		}

		return StatusResponse.success();
	}

	/*@GET
	@Path("/{category:[a-zA-Z_0-9]+}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Task> getTasks(@PathParam("category") final Category category) {
		return taskManager.getTasks(category);
	}*/
}
