package edu.kingsbury.task_tracker;

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
		
		boolean success = false;
		
		try {
			request.login(user.getEmail(), user.getPassword());
			this.userDao.updateLastLogin(request.getUserPrincipal().getName());
			success = true;
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
		return Response
			.status(success ? Response.Status.OK : Response.Status.NOT_FOUND)
			.entity(success ? this.userDao.find(request.getUserPrincipal().getName()) : null)
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
