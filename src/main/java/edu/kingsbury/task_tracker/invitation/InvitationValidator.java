package edu.kingsbury.task_tracker.invitation;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

import edu.kingsbury.task_tracker.Feedback;
import edu.kingsbury.task_tracker.user.User;
import edu.kingsbury.task_tracker.user.UserDao;
import edu.kingsbury.task_tracker.user.UserPostgresDao;

/**
 * Validator for {@link Invitation}.
 * 
 * @author brian
 */
public class InvitationValidator {
	
	/**
	 * The user dao.
	 */
	private UserDao userDao;
	
	/**
	 * Constructor initializes fields.
	 */
	public InvitationValidator() {
		this.userDao = new UserPostgresDao();
	}

	/**
	 * Validates an invitation.
	 * 
	 * @param invitation the invitation to validate
	 * @return the validation feedback
	 */
	public Feedback validateInvitation(Invitation invitation) {
		Feedback feedback = new Feedback();
		
		if (StringUtils.isBlank(invitation.getFirstName())) {
			feedback.getDangerMessages().add("Please enter a first name.");
		}
		if (StringUtils.isBlank(invitation.getLastName())) {
			feedback.getDangerMessages().add("Please enter a last name.");
		}
		if (!EmailValidator.getInstance().isValid(invitation.getEmail())) {
			feedback.getDangerMessages().add("Please enter a valid email address.");
		} else {
			User user = this.userDao.find(invitation.getEmail());
			if (user != null && user.getLastLogin() != null) {
				feedback.getDangerMessages().add("A volunteer with the email address provided is already part of the network.");	
			}
		}
		
		return feedback;
	}
}
