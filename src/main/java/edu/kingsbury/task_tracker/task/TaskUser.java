package edu.kingsbury.task_tracker.task;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import edu.kingsbury.task_tracker.user.User;

/**
 * Represents a task user.
 * 
 * @author brian
 */
public class TaskUser {

	/**
	 * The user.
	 */
	private User user;
	
	/**
	 * The user's task status id.
	 */
	private long statusId;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return this.user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return the statusId
	 */
	public long getStatusId() {
		return this.statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}
}
