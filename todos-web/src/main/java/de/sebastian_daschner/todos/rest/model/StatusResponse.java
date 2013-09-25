package de.sebastian_daschner.todos.rest.model;

public class StatusResponse {

	private boolean success;
	private String message;

	public StatusResponse() {}

	private StatusResponse(final boolean success, final String message) {
		this.success = success;
		this.message = message;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(final boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(final String message) {
		this.message = message;
	}

	public static StatusResponse success() {
		return new StatusResponse(true, null);
	}

	public static StatusResponse error(final String message) {
		return new StatusResponse(false, message);
	}

}
