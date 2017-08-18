package edu.kingsbury.task_tracker.task.comment;

import org.apache.commons.lang3.StringUtils;

import edu.kingsbury.task_tracker.Feedback;

/**
 * Validator for {@link Comment}.
 * 
 * @author brian
 */
public class CommentValidator {

	/**
	 * Validates a comment.
	 * 
	 * @param comment the comment to validate
	 * @return the validation feedback
	 */
	protected Feedback validate(Comment comment) {
		Feedback feedback = new Feedback();
		
		if (StringUtils.isBlank(comment.getText())) {
			feedback.getDangerMessages().add("Please enter a comment.");
		}
		
		return feedback;
	}
}
