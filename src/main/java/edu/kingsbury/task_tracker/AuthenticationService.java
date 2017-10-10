package edu.kingsbury.task_tracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

import edu.kingsbury.task_tracker.user.PasswordChange;
import edu.kingsbury.task_tracker.user.User;
import edu.kingsbury.task_tracker.user.UserDao;
import edu.kingsbury.task_tracker.user.UserPostgresDao;
import edu.kingsbury.task_tracker.user.UserValidator;

@Path("/auth")
public class AuthenticationService {

	/**
	 * The user dao.
	 */
	private UserDao userDao;
	
	/**
	 * The user validator.
	 */
	private UserValidator userValidator;
	
	/**
	 * Constructor initializes the dao.
	 */
	public AuthenticationService() {
		this.userDao = new UserPostgresDao();
		this.userValidator = new UserValidator();
	}
	
	/**
	 * Determines if a user is logged in.
	 * 
	 * @param request the request
	 * @return the logged in user, or a 401 response if not logged in
	 */
	@Path("/logged-in")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response loggedIn(
		@Context HttpServletRequest request) {
		
		request.getSession(true);
		boolean loggedIn = request.getUserPrincipal() != null;
		
		return Response
			.status(loggedIn ? Response.Status.OK : Response.Status.UNAUTHORIZED)
			.entity(loggedIn ? this.userDao.find(request.getUserPrincipal().getName()) : null)
			.build();
	}
	
	/**
	 * Logs a user in.
	 * 
	 * @param user the user
	 * @param request request
	 * @return OK if successful, NOT_FOUND otherwise
	 */
	@Path("/login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(
		User user,
		@Context HttpServletRequest request) {
		
		boolean success;
		User loggedInUser;
		
		try {
			request.login(user.getEmail(), user.getPassword());
			loggedInUser = this.userDao.find(request.getUserPrincipal().getName());
			if (user.isDeleted()) {
				success = false;
				loggedInUser = null;
			} else {
				this.userDao.updateLastLogin(request.getUserPrincipal().getName());
				success = true;
			}
		} catch (ServletException e) {
			success = false;
			loggedInUser = null;
			e.printStackTrace();
		}
		
		return Response
			.status(success ? Response.Status.OK : Response.Status.NOT_FOUND)
			.entity(loggedInUser)
			.build();
	}
	
	@Path("/change-password")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response changePassword(
		PasswordChange passwordChange,
		@Context HttpServletRequest request) {
		
		Feedback feedback = this.userValidator.validatePasswordChange(passwordChange);
		if (feedback.isValid()) {
			User user = this.userDao.find(request.getUserPrincipal().getName());
			boolean success = false;
			success = this.userDao.changePassword(user.getId(), passwordChange.getNewPassword(), passwordChange.getCurrentPassword());
			if (success) {
				return Response
					.status(Response.Status.OK)
					.entity(user)
					.build();
			} else {
				feedback.getDangerMessages().add("The current password provided is incorrect.");
				return Response
					.status(Response.Status.BAD_REQUEST)
					.entity(feedback)
					.build();
			}
		} else {
			return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(feedback)
				.build();
		}
	}
	
	/**
	 * Sends a password reset email for forgotten passwords.
	 * 
	 * @param emailAddress the email address
	 * @return the response
	 * @throws EmailException if an error occurs sending the email
	 * @throws FileNotFoundException if the properties file is not found
	 * @throws IOException if an error occurs reading the properties file
	 */
	@Path("/forgot-password")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response forgotPassword(
		String emailAddress) throws EmailException, FileNotFoundException, IOException {
		
		Object a,b,c;
		User user = this.userDao.find(emailAddress);
		if (user != null) {
			Random random = new Random();
			int code = 100000 + random.nextInt(900000);
			user.setPasswordResetKey(code);
			this.userDao.update(user);
			
			Properties properties = new Properties();
			properties.load(new FileInputStream(System.getProperty("catalina.home") + File.separator + "/conf/kvn.properties"));
			
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.gmail.com");
			email.setSmtpPort(465);
			email.setAuthentication(properties.getProperty("email"), properties.getProperty("password"));
			email.setSSLOnConnect(true);
			email.setFrom(properties.getProperty("email"));
			email.addTo(emailAddress);
			email.setSubject("Kingsbury Volunteer Network - Password Reset");
			StringBuilder message = new StringBuilder();
			message.append("<html><body style='font-family: sans-serif;'>");
			message.append("<p style='margin-bottom: 1em;'>Hello,</p>");
			message.append("<p style='margin-bottom: 1em;'>A password reset was recently requested. To reset your password, click <a href='" + properties.getProperty("passwordResetUrl") + "'>here</a> and enter the code below.");
			message.append("<p style='margin-bottom: 1em; font-weight: bold;'>" + code + "</p>");
			message.append("<p style='margin-bottom: 1em;'>Thanks,</p>");
			message.append("<p style='margin-bottom: 1em;'>Kingsbury Community Volunteer Network</p>");
			message.append("</body></html>");
			email.setMsg(message.toString());
			
			email.send();
		}
		
		 
		Feedback feedback = new Feedback();
		feedback.getInfoMessages().add("Your password reset request has been received. Please check your inbox for further information.");
		return Response
			.status(Response.Status.OK)
			.entity(feedback)
			.build();
	}
	
	/**
	 * Resets a user's password and logs them in.
	 * 
	 * @return the repsonse
	 * @throws ServletException if an error occurs logging in after resetting the password
	 */
	@Path("/reset-password")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response resetPassword(
		User user, 
		@Context HttpServletRequest request) throws ServletException {
		
		Feedback feedback = new Feedback();
		boolean success;
		User matchingUser = this.userDao.find(user.getEmail());
		if (matchingUser == null) {
			success = false;
		} else {
			success = this.userDao.resetPassword(matchingUser.getId(), user.getPassword(), user.getPasswordResetKey());
		}
		
		if (success) {
			request.login(user.getEmail(), user.getPassword());
		} else {
			feedback.getDangerMessages().add("Sorry, no account was found matching the email address and code provided.");
		}
		
		return Response
			.status(feedback.isValid() ? Response.Status.OK : Response.Status.NOT_FOUND)
			.entity(feedback)
			.build();
	}
	
	/**
	 * Logs a user out.
	 * 
	 * @param request the request
	 * @return OK if successful, BAD_REQUEST otherwise
	 */
	@POST
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public Response logout(
		@Context HttpServletRequest request) {
		
		boolean success = false;
		try {
			request.logout();
			success = true;
		} catch (ServletException e) {
			e.printStackTrace();
		}
		return Response
				.status(success ? Response.Status.OK : Response.Status.BAD_REQUEST)
				.entity(null)
				.build(); 
	}
}
