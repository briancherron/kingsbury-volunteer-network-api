package edu.kingsbury.task_tracker.user;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import edu.kingsbury.task_tracker.Feedback;
import edu.kingsbury.task_tracker.category.Category;

/**
 * Web service for {@link User}.
 * 
 * @author brian
 */
@Path("/users")
public class UserService {
	
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
	public UserService() {
		this.userDao = new UserPostgresDao();
		this.userValidator = new UserValidator();
	}

	/**
	 * Finds all users.
	 * 
	 * @return all users
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		return Response
			.status(Response.Status.OK)
			.entity(this.userDao.findAll())
			.build();
	}
	
	/**
	 * Finds a user.
	 * 
	 * @param id the user id
	 * @return the user
	 */
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(
		@PathParam("id") long id) {
		
		Response.Status status = Response.Status.OK;
		User user = this.userDao.find(id);
		
		if (user == null) {
			status = Response.Status.NOT_FOUND;
		}
		
		return Response
			.status(status)
			.entity(user)
			.build();
	}
	
	/**
	 * Creates a user.
	 * 
	 * @param user the user
	 * @param request the request
	 * @return the created user
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(
		User user,
		@Context HttpServletRequest request) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.userDao.create(user))
			.build();
	}
	
	/**
	 * Updates a user.
	 * 
	 * @param id the user id
	 * @param user the user
	 * @param request the request
	 * @return the updated user
	 */
	@Path("/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(
		@PathParam("id") long id,
		User user,
		@Context HttpServletRequest request) {
		
		Feedback feedback = this.userValidator.validateUser(user, false);
		if (feedback.isValid()) {
			return Response
				.status(Response.Status.OK)
				.entity(this.userDao.update(user))
				.build();
		} else {
			return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(feedback)
				.build();
		}
		
	}
	
	@Path("/{id}/interests-or-skills")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateInterestsOrSkills(@PathParam("id") long id,
		List<Category> categories) {
	
		this.userDao.removeCategories(id);
		this.userDao.addCategories(id, categories);
		
		return Response
			.status(Response.Status.OK)
			.entity(new Feedback())
			.build();
	}
	
	@Path("/join")
	@PUT
	@Produces(MediaType.APPLICATION_JSON) 
	public Response join(
		User user,
		@Context HttpServletRequest request) throws ServletException {
		
		// first validate the user
		Feedback feedback = this.userValidator.validateUser(user, true);
		if (feedback.isValid()) {
		
			// then update the user
			this.userDao.update(user);
			
			// then update the user's password
			this.userDao.changePassword(user.getId(), user.getPassword(), null);
			
			// then log the user in and update the last login
			request.login(user.getEmail(), user.getPassword());
			this.userDao.updateLastLogin(user.getEmail());
			
			return Response
				.status(Response.Status.OK)
				.entity(this.userDao.find(user.getId()))
				.build();
		} else {
			return Response
				.status(Response.Status.BAD_REQUEST)
				.entity(feedback)
				.build();
		}
	}
	
	/**
	 * Deletes a user.
	 * 
	 * @param id the user id
	 * @param request the request
	 * @return the deleted user
	 */
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.userDao.delete(id))
			.build();
	}
}
