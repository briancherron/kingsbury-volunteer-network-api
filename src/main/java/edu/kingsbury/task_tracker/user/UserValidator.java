package edu.kingsbury.task_tracker.user;


import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import edu.kingsbury.task_tracker.Feedback;

/**
 * Performs validation for {@link User}.
 * 
 * @author brian
 */
public class UserValidator {

	/**
	 * Validates a user.
	 * 
	 * @param user the user to validate
	 * @param joining whether the user is joining
	 * @return the validation feedback
	 */
	public Feedback validateUser(User user, boolean joining) {
		Feedback feedback = new Feedback();
		this.validateEmail(feedback, user.getEmail());
		this.validateFirstName(feedback, user.getFirstName());
		this.validateLastName(feedback, user.getLastName());
		this.validatePhone(feedback, user);
		if (joining) {
			this.validatePassword(feedback, user.getPassword());
		}
		
		return feedback;
	}
	
	public Feedback validatePasswordChange(PasswordChange passwordChange) {
		Feedback feedback = new Feedback();
		if (!StringUtils.equals(passwordChange.getNewPassword(), passwordChange.getNewPasswordConfirm())) {
			feedback.getDangerMessages().add("The new passwords do not match.");
		} else {
			this.validatePassword(feedback, passwordChange.getNewPassword());
		}
		
		return feedback;
	}
	
	private void validatePassword(Feedback feedback, String password) {
		if (password.length() < 8) {
			feedback.getDangerMessages().add("The new password must be at least 8 characters long.");
		} else if (!Pattern.compile("[a-zA-Z]+").matcher(password).find() || !Pattern.compile("\\d+").matcher(password).find()) {
			feedback.getDangerMessages().add("The new password must contain at least one letter and one number.");
		}
	}
	
	/**
	 * Validates the email.
	 * 
	 * @param feedback the feedback
	 * @param email the email
	 */
	private void validateEmail(Feedback feedback, String email) {
		if (!EmailValidator.getInstance().isValid(email)) {
			feedback.getDangerMessages().add("Please enter a valid email address.");
		}
	}
	
	/**
	 * Validates the first name.
	 * 
	 * @param feedback the feedback
	 * @param firstName the first name
	 */
	private void validateFirstName(Feedback feedback, String firstName) {
		if (StringUtils.isBlank(firstName)) {
			feedback.getDangerMessages().add("Please enter a first name.");
		}
	}
	
	/**
	 * Validates the last name.
	 * 
	 * @param feedback the feedback
	 * @param lastName the last name
	 */
	public void validateLastName(Feedback feedback, String lastName) {
		if (StringUtils.isBlank(lastName)) {
			feedback.getDangerMessages().add("Please enter a last name.");
		}
	}
	
	/**
	 * Validates the phone.
	 * 
	 * @param feedback the feedback
	 * @param phone the phone
	 */
	private void validatePhone(Feedback feedback, User user) {
		user.setPhone(user.getPhone().replaceAll("\\D+", ""));
		if (!Pattern.compile("\\d{10}").matcher(user.getPhone()).matches()) {
			feedback.getDangerMessages().add("Please enter a valid phone number.");
		}
	}
}
