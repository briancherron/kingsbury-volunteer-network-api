package edu.kingsbury.task_tracker.user;

import java.util.List;

/**
 * Data access object for {@link User}.
 * 
 * @author brian
 */
public interface UserDao {

	/**
	 * Finds all (non-deleted) users.
	 * 
	 * @return all users
	 */
	List<User> findAll();
	
	/**
	 * Finds a user
	 * 
	 * @param id the user id
	 * @return the user
	 */
	User find(long id);
	
	/**
	 * Finds a user.
	 * 
	 * @param email the email address
	 * @return the user
	 */
	User find(String email);
	
	/**
	 * Updates the last login for a user.
	 * 
	 * @param email the user email address
	 */
	void updateLastLogin(String email);
	
	/**
	 * Creates a new user.
	 * 
	 * @param user the user to create
	 * @return the user
	 */
	User create(User user);
	
	/**
	 * Updates a user.
	 * 
	 * @param user the user to update
	 * @return the user
	 */
	User update(User user);
	
	/**
	 * Changes a user's password.
	 * 
	 * @param id the user id
	 * @param newPassword the new password
	 * @param currentPassword the current password
	 * @return <code>true</code> if the password is changed
	 */
	boolean changePassword(long id, String newPassword, String currentPassword);
	
	/**
	 * Deletes a user.
	 * 
	 * @param id the user id
	 * @return <code>true</code> if the user is deleted
	 */
	boolean delete(long id);
}
