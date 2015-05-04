package model.exceptions;

import model.tasks.Task;

/**
 * An exception that is thrown when a task that is not possible
 * is attempted. This includes placing a structure on an invalid
 * tile or attempting to build a structure with insufficient resources
 * @author Christopher
 *
 */
public class DisallowedTaskException extends RuntimeException {

	private static final long serialVersionUID = -4791950896100974091L;
	private Task sourceTask;
	private String message;

	public DisallowedTaskException( Task task, String message ) {
		this.sourceTask = task;
		this.message = message;
	}
	
	public Task getSourceTask() {
		return this.sourceTask;
	}
	
	public String getMessage() {
		return this.message;
	}
}
