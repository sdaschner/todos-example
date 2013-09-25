package de.sebastian_daschner.todos.rest.model;

import java.util.Date;

import javax.ws.rs.QueryParam;

public class TaskBeanParam {

	@QueryParam("description")
	private String description;

	@QueryParam("dueDate")
	private Long dueDate;

	public String getDescription() {
		return description;
	}

	public Date getDueDate() {
		if (dueDate == null) {
			return null;
		}

		final Date date = new Date();
		date.setTime(dueDate * 1000);

		return date;
	}
}
