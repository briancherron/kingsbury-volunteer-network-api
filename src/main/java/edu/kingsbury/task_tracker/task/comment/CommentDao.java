package edu.kingsbury.task_tracker.task.comment;

import java.util.List;

/**
 * Data access object for {@link Comment}.
 * 
 * @author brian
 */
public interface CommentDao {

	/**
	 * Adds a comment.
	 * 
	 * @param comment the comment
	 * @return the added comment
	 */
	Comment add(Comment comment);
	
	/**
	 * Edits a comment.
	 * 
	 * @param comment the comment
	 * @return the edited comment
	 */
	Comment edit(Comment comment);
	
	/**
	 * Deletes a comment.
	 * 
	 * @param commentId the comment id
	 * @param userId the user id
	 * @return the deleted comment
	 */
	Comment delete(long commentId, long userId);
	
	/**
	 * Finds a comment.
	 * 
	 * @param id the id
	 * @return the comment
	 */
	Comment find(long id);
	
	/**
	 * Finds comments for a task.
	 * 
	 * @param taskId the task id
	 * @return the task comments
	 */
	List<Comment> findComments(long taskId);
}
