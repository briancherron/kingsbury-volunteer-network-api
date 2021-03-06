package edu.kingsbury.task_tracker.task;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Represents an audience.
 */
public class Audience {

	/**
	 * The audience id.
	 */
	private long id;
	
	/**
	 * The audience name.
	 */
	private String name;
	
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
}
