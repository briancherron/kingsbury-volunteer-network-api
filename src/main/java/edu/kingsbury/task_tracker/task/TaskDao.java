package edu.kingsbury.task_tracker.task;

import java.util.List;

import edu.kingsbury.task_tracker.category.Category;

/**
 * Data access object for {@link Task}.
 * 
 * @author brian
 */
public interface TaskDao {

	/**
	 * Finds all tasks.
	 * 
	 * @param filter the task list filter
	 * @param adminUser whether the logged in user is an admin user
	 * @return all tasks
	 */
	List<Task> find(Filter filter, boolean adminUser);
	
	/**
	 * Finds all task statuses.
	 * 
	 * @return all task statuses
	 */
	List<TaskStatus> findAllStatuses();
	
	/**
	 * Finds all audiences.
	 * 
	 * @return all audiences
	 */
	List<Audience> findAllAudiences();
	
	/**
	 * Finds a task by id.
	 * 
	 * @param id the task id
	 * @param adminUser whether the logged in user is an admin user
	 * @return the task
	 */
	Task find(long id, boolean adminUser);
	
	/**
	 * Creates a task.
	 * 
	 * @param task the task to create
	 * @param userEmail the email of the user creating the task
	 * @return the created task
	 */
	Task create(Task task, String userEmail);
	
	/**
	 * Updates a task.
	 * 
	 * @param task the task to update
	 * @param userEmail the email of the user updating the task
	 * @return the updated task
	 */
	Task update(Task task, String userEmail);
	
	/**
	 * Adds categories to a task.
	 * 
	 * @param taskId the task id
	 * @param categories the categories to add
	 */
	void addCategories(long taskId, List<Category> categories);
	
	/**
	 * Removes categories from  a task.
	 * 
	 * @param taskId the task id
	 */
	void removeCategories(long taskId);
	
	/**
	 * Adds a user to a task.
	 * 
	 * @param taskId the task id
	 * @param userId the user id
	 * @param statusId the task user status id
	 */
	void addUser(long taskId, long userId, long statusId);
	
	/**
	 * Removes a user from a task.
	 * 
	 * @param taskId the task id
	 * @param userId the user id
	 */
	void removeUser(long taskId, long userId);
	
	/**
	 * Deletes a task.
	 * 
	 * @param taskId the task id
	 * @param userEmail the email of the user deleting the task
	 * @param adminUser whether the logged in user is an admin
	 * @return <code>true</code> if the task is deleted
	 */
	Task delete(long taskId, String userEmail, boolean adminUser);
}
