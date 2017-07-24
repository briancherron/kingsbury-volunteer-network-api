package edu.kingsbury.task_tracker.category;

import java.util.List;

/**
 * Data access object for {@link Category}.
 * 
 * @author brian
 */
public interface CategoryDao {

	/**
	 * Finds all categories.
	 * 
	 * @return all categories
	 */
	List<Category> findAll();
	
	/**
	 * Finds a category
	 * 
	 * @param id the category id
	 * @return the category
	 */
	Category find(long id);
	
	/**
	 * Creates a new category.
	 * 
	 * @param category the category to create
	 * @return the category
	 */
	Category create(Category category);
	
	/**
	 * Updates a category.
	 * 
	 * @param category the category to update
	 * @return the category
	 */
	Category update(Category category);
	
	/**
	 * Deletes a category.
	 * 
	 * @param id the category id
	 * @return <code>true</code> if the category is deleted
	 */
	boolean delete(long id);
}
