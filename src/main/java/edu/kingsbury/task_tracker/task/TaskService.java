package edu.kingsbury.task_tracker.task;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BeanParam;
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
import javax.ws.rs.core.Response.Status;

/**
 * Web service for {@link Task}.
 * 
 * @author brian
 */
@Path("/tasks")
public class TaskService {
	
	/**
	 * The task dao.
	 */
	private TaskDao taskDao;
	
	/**
	 * Constructor initializes the dao.
	 */
	public TaskService() {
		this.taskDao = new TaskPostgresDao();
	}

	/**
	 * Finds all tasks.
	 * 
	 * @return all tasks
	 */
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(@BeanParam Filter filter) {
		return Response
			.status(Response.Status.OK)
			.entity(this.taskDao.find(filter))
			.build();
	}
	
	@GET
	@Path("/statuses")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findStatuses() {
		return Response
			.status(Response.Status.OK)
			.entity(this.taskDao.findAllStatuses())
			.build();
	}
	
	/**
	 * Finds a task.
	 * 
	 * @param id the task id
	 * @return the matching task
	 */
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(
		@PathParam("id") long id) {
		
		Status status = Response.Status.OK;
		Task task = this.taskDao.find(id);
		if (status == null) {
			status = Response.Status.NOT_FOUND;
		}
		
		return Response
			.status(status)
			.entity(task)
			.build();
	}
	
	/**
	 * Creates a task.
	 * 
	 * @param task the task to create
	 * @param request the request
	 * @return the created task
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(
		Task task,
		@Context HttpServletRequest request) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.taskDao.create(task, request.getUserPrincipal().getName()))
			.build();
	}
	
	/**
	 * Updates a task.
	 * 
	 * @param task the task to update
	 * @param id the task id
	 * @param request the request
	 * @return the updated task
	 */
	@PUT
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response update(
		Task task,
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		return Response
			.status(Response.Status.OK)
			.entity(this.taskDao.update(task, request.getUserPrincipal().getName()))
			.build();
	}
	
	/**
	 * Deletes a task.
	 * 
	 * @param id the task id
	 * @param request the request
	 * @return whether the task was deleted
	 */
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response delete(
		@PathParam("id") long id,
		@Context HttpServletRequest request) {
		
		Task task = this.taskDao.delete(id, request.getUserPrincipal().getName());
		return Response
			.status(task.isDeleted() ? Response.Status.OK : Response.Status.NOT_FOUND)
			.entity(task)
			.build();
	}
}
