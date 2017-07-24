package edu.kingsbury.task_tracker.task;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

import edu.kingsbury.task_tracker.category.Category;
import edu.kingsbury.task_tracker.partner.Partner;
import edu.kingsbury.task_tracker.user.User;

/**
 * Represents a task.
 * 
 * @author brian
 */
public class Task {

	/**
	 * The task id.
	 */
	private long id;
	
	/**
	 * The task name.
	 */
	private String name;
	
	/**
	 * The task date.
	 */
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="EST")
	private Date date;
	
	/**
	 * The task decription.
	 */
	private String description;
	
	/**
	 * The task status.
	 */
	private TaskStatus status;
	
	/**
	 * The task users.
	 */
	private List<User> users;
	
	/**
	 * The task partners.
	 */
	private List<Partner> partners;
	
	/**
	 * The task categories.
	 */
	private List<Category> categories;
	
	/**
	 * Whether the task is deleted.
	 */
	private boolean deleted;
	
	/**
	 * The date the task was added.
	 */
	private Date dateAdded;
	
	/**
	 * The id of the user adding the task.
	 */
	private User userAdded;
	
	/**
	 * The date the task was last modified.
	 */
	private Date dateModified;
	
	/**
	 * The id of the user modifying the task.
	 */
	private User userModified;
	
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
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the date
	 */
	public Date getDate() {
		return this.date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the status
	 */
	public TaskStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(TaskStatus status) {
		this.status = status;
	}

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return this.users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		this.users = users;
	}

	/**
	 * @return the partners
	 */
	public List<Partner> getPartners() {
		return this.partners;
	}

	/**
	 * @param partners the partners to set
	 */
	public void setPartners(List<Partner> partners) {
		this.partners = partners;
	}

	/**
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return this.categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
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
	 * @return the userAdded
	 */
	public User getUserAdded() {
		return this.userAdded;
	}

	/**
	 * @param userAdded the userAdded to set
	 */
	public void setUserAdded(User userAdded) {
		this.userAdded = userAdded;
	}

	/**
	 * @return the dateModified
	 */
	public Date getDateModified() {
		return this.dateModified;
	}

	/**
	 * @param dateModified the dateModified to set
	 */
	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	/**
	 * @return the userModified
	 */
	public User getUserModified() {
		return this.userModified;
	}

	/**
	 * @param userModified the userModified to set
	 */
	public void setUserModified(User userModified) {
		this.userModified = userModified;
	}
}
