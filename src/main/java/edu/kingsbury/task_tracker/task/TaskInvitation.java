package edu.kingsbury.task_tracker.task;

import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Represents a task invitation.
 * 
 * @author brian
 */
public class TaskInvitation {
	
	/**
	 * The users to invite.
	 */
	private List<TaskUser> invitees;
	
	/**
	 * The invitation message.
	 */
	private String message;
	
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * @return the invitees
	 */
	public List<TaskUser> getInvitees() {
		return this.invitees;
	}

	/**
	 * @param invitees the invitees to set
	 */
	public void setInvitees(List<TaskUser> invitees) {
		this.invitees = invitees;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
