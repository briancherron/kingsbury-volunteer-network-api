package edu.kingsbury.task_tracker.task;

import javax.ws.rs.QueryParam;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Represents a filter for tasks.
 * 
 * @author brian
 */
public class Filter {
	
	/**
	 * The search query.
	 */
	@QueryParam("query")
	private String query;

	/**
	 * The task status id.
	 */
	@QueryParam("statusId")
	private long statusId;
	
	/**
	 * The user id.
	 */
	@QueryParam("userId")
	private long userId;
	
	/**
	 * The category id.
	 */
	@QueryParam("categoryId")
	private long categoryId;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return new ReflectionToStringBuilder(this).toString();
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return this.query;
	}

	/**
	 * @param query the query to set
	 */
	public void setQuery(String query) {
		this.query = query;
	}

	/**
	 * @return the statusId
	 */
	public long getStatusId() {
		return this.statusId;
	}

	/**
	 * @param statusId the statusId to set
	 */
	public void setStatusId(long statusId) {
		this.statusId = statusId;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return this.userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the categoryId
	 */
	public long getCategoryId() {
		return this.categoryId;
	}

	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}
}
