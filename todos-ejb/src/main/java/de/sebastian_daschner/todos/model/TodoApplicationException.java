package de.sebastian_daschner.todos.model;

public class TodoApplicationException extends Exception {

    public TodoApplicationException() {
        super();
    }

    public TodoApplicationException(final String message) {
        super(message);
    }

    public TodoApplicationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public TodoApplicationException(final Throwable cause) {
        super(cause);
    }

}
