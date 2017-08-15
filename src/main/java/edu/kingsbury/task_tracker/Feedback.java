package edu.kingsbury.task_tracker;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Represents feedback from form submission.
 * 
 * @author brian
 * 
 */
public class Feedback {
	
	//TODO: make this class generic, and return feedback from every web service, embedding the necessary object
	
	/**
	 * The success feedback messages.
	 */
	private final List<String> successMessages;
	
	/**
	 * The info feedback messages.
	 */
	private final List<String> infoMessages;
	
	/**
	 * The warning feedback messages.
	 */
	private final List<String> warningMessages;
	
	/**
	 * The danger feedback messages.
	 */
	private final List<String> dangerMessages;
	
	/**
	 * Constructor initializes fields.
	 */
	public Feedback() {
		this.successMessages = new ArrayList<String>();
		this.infoMessages = new ArrayList<String>();
		this.warningMessages = new ArrayList<String>();
		this.dangerMessages = new ArrayList<String>();
	}
	
	/**
	 * @return <code>true</code> if there are no danger or warning messages
	 */
	public boolean isValid() {
		return this.dangerMessages.isEmpty() && this.warningMessages.isEmpty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * @return the successMessages
	 */
	public List<String> getSuccessMessages() {
		return this.successMessages;
	}

	/**
	 * @return the infoMessages
	 */
	public List<String> getInfoMessages() {
		return this.infoMessages;
	}

	/**
	 * @return the warningMessages
	 */
	public List<String> getWarningMessages() {
		return this.warningMessages;
	}

	/**
	 * @return the dangerMessages
	 */
	public List<String> getDangerMessages() {
		return this.dangerMessages;
	}
}
