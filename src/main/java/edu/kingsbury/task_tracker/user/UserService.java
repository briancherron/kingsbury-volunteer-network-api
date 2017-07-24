package edu.kingsbury.task_tracker.user;

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
	 * Constructor initializes the dao.
	 */
	public UserService() {
		this.userDao = new UserPostgresDao();
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
		
		return Response
			.status(Response.Status.OK)
			.entity(this.userDao.update(user))
			.build();
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
