package edu.kingsbury.task_tracker;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.kingsbury.task_tracker.user.User;
import edu.kingsbury.task_tracker.user.UserDao;
import edu.kingsbury.task_tracker.user.UserPostgresDao;

@Path("/auth")
public class AuthenticationService {

	/**
	 * The user dao.
	 */
	private UserDao userDao;
	
	/**
	 * Constructor initializes the dao.
	 */
	public AuthenticationService() {
		this.userDao = new UserPostgresDao();
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
