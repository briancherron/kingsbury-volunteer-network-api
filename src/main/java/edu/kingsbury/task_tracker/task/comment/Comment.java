package edu.kingsbury.task_tracker.task.comment;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import edu.kingsbury.task_tracker.user.User;

/**
 * Represents a comment.
 * 
 * @author brian
 */
public class Comment {

	/**
	 * The comment id.
	 */
	private long id;
	
	/**
	 * The task id.
	 */
	private long taskId;
	
	/**
	 * The comment text.
	 */
	private String text;
	
	/**
	 * The  user.
	 */
	private User user;
	
	/**
	 * The date added.
	 */
	private Date dateAdded;
	
	/**
	 * The date edited.
	 */
	private Date dateEdited;
	
	/**
	 * Whether the comment is deleted.
	 */
	private boolean deleted;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return this.id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the taskId
	 */
	public long getTaskId() {
		return this.taskId;
	}

	/**
	 * @param taskId the taskId to set
	 */
	public void setTaskId(long taskId) {
		this.taskId = taskId;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
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
	 * @return the dateAdded
	 */
	public Date getDateAdded() {
		return this.dateAdded;
	}

	/**
	 * @param dateAdded the dateAdded to set
	 */
	public void setDateAdded(Date dateAdded) {
		this.dateAdded = dateAdded;
	}

	/**
	 * @return the dateEdited
	 */
	public Date getDateEdited() {
		return this.dateEdited;
	}

	/**
	 * @param dateEdited the dateEdited to set
	 */
	public void setDateEdited(Date dateEdited) {
		this.dateEdited = dateEdited;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return this.deleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
