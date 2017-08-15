package edu.kingsbury.task_tracker.invitation;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Represents an invitation.
 * 
 * @author brian
 */
public class Invitation {
	
	/**
	 * The invitee's first name.
	 */
	private String firstName;
	
	/**
	 * The invitee's last name.
	 */
	private String lastName;
	
	/**
	 * The invitee's email address.
	 */
	private String email;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
}
