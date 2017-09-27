package edu.kingsbury.task_tracker.introduction;

/**
 * Performs data access for the app introduction.
 * 
 * @author brian
 */
public interface IntroductionDao {

	/**
	 * Finds the introduction.
	 * 
	 * @return the introduction
	 */
	String findIntroduction();
	
	/**
	 * Saves the introduction.
	 * 
	 * @param introduction the introduction
	 */
	void saveIntroduction(String introduction);
}
