package edu.kingsbury.task_tracker.partner;

import java.util.Date;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Represents a community partner. Community partners are local businesses and organizations that may assist with 
 * tasks.
 * 
 * @author brian
 */
public class Partner {

	/**
	 * The partner id.
	 */
	private long id;
	
	/**
	 * The partner email address.
	 */
	private String email;
	
	/**
	 * The partner phone number.
	 */
	private String phone;
	
	/**
	 * The partner name.
	 */
	private String name;
	
	/**
	 * The first name of the contact at the partner.
	 */
	private String contactFirstName;
	
	/**
	 * The last name of the contact at the partner.
	 */
	private String contactLastName;
	
	/**
	 * The partner's facebook name.
	 */
	private String facebook;
	
	/**
	 * Whether the partner is choosing to be recognized.
	 */
	private boolean recognitionOptIn;
	
	/**
	 * Whether the partner is deleted.
	 */
	private boolean deleted;
	
	/**
	 * The date the partner was added.
	 */
	private Date dateAdded;
	
	/**
	 * The id of the user adding the partner.
	 */
	private long userAdded;
	
	/**
	 * The date the partner was last modified.
	 */
	private Date dateModified;
	
	/**
	 * The id of the user modifying the partner.
	 */
	private long userModified;
	
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
	 * @return the email
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return this.phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
	 * @return the contactFirstName
	 */
	public String getContactFirstName() {
		return this.contactFirstName;
	}

	/**
	 * @param contactFirstName the contactFirstName to set
	 */
	public void setContactFirstName(String contactFirstName) {
		this.contactFirstName = contactFirstName;
	}

	/**
	 * @return the contactLastName
	 */
	public String getContactLastName() {
		return this.contactLastName;
	}

	/**
	 * @param contactLastName the contactLastName to set
	 */
	public void setContactLastName(String contactLastName) {
		this.contactLastName = contactLastName;
	}

	/**
	 * @return the facebook
	 */
	public String getFacebook() {
		return this.facebook;
	}

	/**
	 * @param facebook the facebook to set
	 */
	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	/**
	 * @return the recognitionOptIn
	 */
	public boolean isRecognitionOptIn() {
		return this.recognitionOptIn;
	}

	/**
	 * @param recognitionOptIn the recognitionOptIn to set
	 */
	public void setRecognitionOptIn(boolean recognitionOptIn) {
		this.recognitionOptIn = recognitionOptIn;
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
	public long getUserAdded() {
		return this.userAdded;
	}

	/**
	 * @param userAdded the userAdded to set
	 */
	public void setUserAdded(long userAdded) {
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
	public long getUserModified() {
		return this.userModified;
	}

	/**
	 * @param userModified the userModified to set
	 */
	public void setUserModified(long userModified) {
		this.userModified = userModified;
	}
}
