package de.sebastian_daschner.todos.model;

public class TaskManagerException extends TodoApplicationException {

	public TaskManagerException() {
		super();
	}

	public TaskManagerException(final String message) {
		super(message);
	}

	public TaskManagerException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public TaskManagerException(final Throwable cause) {
		super(cause);
	}

}
