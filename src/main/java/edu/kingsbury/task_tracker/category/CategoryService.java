package edu.kingsbury.task_tracker.category;

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
 * Web service for {@link Category}.
 * 
 * @author brian
 */
@Path("/categories")
public class CategoryService {
	
	/**
	 * The category dao.
	 */
	private CategoryDao categoryDao;
	
	/**
	 * Constructor initializes the dao.
	 */
	public CategoryService() {
		this.categoryDao = new CategoryPostgresDao();
	}

	/**
	 * Finds all categories.
	 * 
	 * @return all categories
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAll() {
		return Response
			.status(Response.Status.OK)
			.entity(this.categoryDao.findAll())
			.build();
	}
	
	/**
	 * Finds a category.
	 * 
	 * @param id the category id
	 * @return the category
	 */
	@Path("/{id}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(
		@PathParam("id") long id) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.categoryDao.find(id))
			.build();
	}
	
	/**
	 * Creates a category.
	 * 
	 * @param category the category
	 * @param request the request
	 * @return the created category
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(
		Category category,
		@Context HttpServletRequest request) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.categoryDao.create(category))
			.build();
	}
	
	/**
	 * Updates a category.
	 * 
	 * @param id the category id
	 * @param category the category
	 * @param request the request
	 * @return the updated category
	 */
	@Path("/{id}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(
		@PathParam("id") long id,
		Category category,
		@Context HttpServletRequest request) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.categoryDao.update(category))
			.build();
	}
	
	/**
	 * Deletes a category.
	 * 
	 * @param id the category id
	 * @param request the request
	 * @return the deleted category
	 */
	@Path("/{id}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.categoryDao.delete(id))
			.build();
	}
}
