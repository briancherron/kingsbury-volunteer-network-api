package edu.kingsbury.task_tracker.task;

import org.apache.commons.lang3.StringUtils;

import edu.kingsbury.task_tracker.Feedback;

/**
 * Validates a {@link Task}.
 * 
 * @author brian
 */
public class TaskValidator {
	
	/**
	 * The max allowable task name length.
	 */
	private static final int MAX_TASK_NAME_LENGTH = 100;

	/**
	 * Validates a task
	 * 
	 * @param task the task to validate
	 * @return the validation feedback
	 */
	public Feedback validate(Task task) {
		Feedback feedback = new Feedback();
		this.validateName(feedback, task.getName());
		this.validateAudience(feedback, task.getAudience());
		
		return feedback;
	}
	
	/**
	 * Validates a task name.
	 * 
	 * @param feedback the feedback to append to
	 * @param taskName the task name
	 */
	private void validateName(Feedback feedback, String taskName) {
		if (StringUtils.isBlank(taskName)) {
			feedback.getDangerMessages().add("Please enter a task name.");
		} else if (StringUtils.length(taskName) > 100) {
			feedback.getDangerMessages().add("A task name cannot be more than " + MAX_TASK_NAME_LENGTH + " characters long.");
		}
	}
	
	private void validateAudience(Feedback feedback, Audience audience) {
		if (audience.getId() == 0) {
			feedback.getDangerMessages().add("Please select an audience.");
		}
	}
}
