package edu.kingsbury.task_tracker.user;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import edu.kingsbury.task_tracker.category.Category;

/**
 * Represents a user in the system.
 * 
 * @author brian
 */
@JsonIgnoreProperties(value = "admin", allowGetters=true)
public class User {

	/**
	 * The user id.
	 */
	private long id;
	
	/**
	 * The user email address.
	 */
	private String email;
	
	/**
	 * The password.
	 */
	private String password;
	
	/**
	 * The user phone number.
	 */
	private String phone;
	
	/**
	 * The user first name.
	 */
	private String firstName;
	
	/**
	 * The user last name.
	 */
	private String lastName;
	
	/**
	 * The user's facebook name.
	 */
	private String facebook;
	
	/**
	 * Whether the user has opted in to be recognized.
	 */
	private boolean recognitionOptIn;
	
	/**
	 * The categories the user is skilled or has interest in.
	 */
	private List<Category> categories;
	
	/**
	 * The last login date/time.
	 */
	private Date lastLogin;
	
	/**
	 * Whether the user is deleted.
	 */
	private boolean deleted;
	
	/**
	 * The invitation key.
	 */
	private UUID invitationKey;
	
	/**
	 * The role id.
	 */
	private long roleId;
	
	/**
	 * Whether the user is authorized to change the introduction.
	 */
	private boolean authorizedToChangeIntroduction;
	
	/**
	 * @return whether the user is an admin
	 */
	public boolean isAdmin() {
		return this.roleId == 1;
	}
	
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
	 * @return the password
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the firstName
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
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
	 * @return the categories
	 */
	public List<Category> getCategories() {
		return this.categories;
	}

	/**
	 * @param categories the categories to set
	 */
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	/**
	 * @return the lastLogin
	 */
	public Date getLastLogin() {
		return this.lastLogin;
	}

	/**
	 * @param lastLogin the lastLogin to set
	 */
	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
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
	 * @return the invitationKey
	 */
	public UUID getInvitationKey() {
		return this.invitationKey;
	}

	/**
	 * @param invitationKey the invitationKey to set
	 */
	public void setInvitationKey(UUID invitationKey) {
		this.invitationKey = invitationKey;
	}

	/**
	 * @return the roleId
	 */
	public long getRoleId() {
		return this.roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(long roleId) {
		this.roleId = roleId;
	}

	/**
	 * @return the authorizedToChangeIntroduction
	 */
	public boolean isAuthorizedToChangeIntroduction() {
		return this.authorizedToChangeIntroduction;
	}

	/**
	 * @param authorizedToChangeIntroduction the authorizedToChangeIntroduction to set
	 */
	public void setAuthorizedToChangeIntroduction(boolean authorizedToChangeIntroduction) {
		this.authorizedToChangeIntroduction = authorizedToChangeIntroduction;
	}
}
