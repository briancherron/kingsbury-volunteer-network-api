package edu.kingsbury.task_tracker.category;

import org.apache.commons.lang3.StringUtils;

import edu.kingsbury.task_tracker.Feedback;

/**
 * Validates a {@link Category}.
 * 
 * @author brian
 */
public class CategoryValidator {
	
	/**
	 * The max allowable category name length.
	 */
	private static final int MAX_CATEGORY_NAME_LENGTH = 50;

	/**
	 * Validates a category
	 * 
	 * @param category the category to validate
	 * @return the validation feedback
	 */
	public Feedback validate(Category category) {
		Feedback feedback = new Feedback();
		this.validateName(feedback, category.getName());
		
		return feedback;
	}
	
	/**
	 * Validates a category name.
	 * 
	 * @param feedback the feedback to append to
	 * @param categoryName the category name
	 */
	private void validateName(Feedback feedback, String categoryName) {
		if (StringUtils.isBlank(categoryName)) {
			feedback.getDangerMessages().add("A skill or interest name is required.");
		} else if (StringUtils.length(categoryName) > MAX_CATEGORY_NAME_LENGTH) {
			feedback.getDangerMessages().add("A skill or interest name cannot be more than " + MAX_CATEGORY_NAME_LENGTH + " characters long.");
		}
	}
}
