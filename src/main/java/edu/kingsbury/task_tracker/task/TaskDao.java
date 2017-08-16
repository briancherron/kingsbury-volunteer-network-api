package edu.kingsbury.task_tracker.task;

import java.util.List;

/**
 * Data access object for {@link Task}.
 * 
 * @author brian
 */
public interface TaskDao {

	/**
	 * Finds all tasks.
	 * 
	 * @return all tasks
	 */
	List<Task> find(Filter filter);
	
	/**
	 * Finds all task statuses.
	 * 
	 * @return all task statuses
	 */
	List<TaskStatus> findAllStatuses();
	
	/**
	 * Finds a task by id.
	 * 
	 * @param id the task id
	 * @return the task
	 */
	Task find(long id);
	
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
	 * @return <code>true</code> if the task is deleted
	 */
	Task delete(long taskId, String userEmail);
}
