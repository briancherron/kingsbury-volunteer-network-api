package edu.kingsbury.task_tracker.user;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Represents the data necessary to change a password.
 * 
 * @author brian
 */
public class PasswordChange {

	/**
	 * The current password.
	 */
	private String currentPassword;
	
	/**
	 * The new password.
	 */
	private String newPassword;
	
	/**
	 * The new password confirmation.
	 */
	private String newPasswordConfirm;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * @return the currentPassword
	 */
	public String getCurrentPassword() {
		return this.currentPassword;
	}

	/**
	 * @param currentPassword the currentPassword to set
	 */
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return this.newPassword;
	}

	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	/**
	 * @return the newPasswordConfirm
	 */
	public String getNewPasswordConfirm() {
		return this.newPasswordConfirm;
	}

	/**
	 * @param newPasswordConfirm the newPasswordConfirm to set
	 */
	public void setNewPasswordConfirm(String newPasswordConfirm) {
		this.newPasswordConfirm = newPasswordConfirm;
	}
}
